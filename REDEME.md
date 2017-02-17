# GreenDao 3.2.0 教程

本教程涉及GreenDao 3.2.0的接入、使用以及更新。

## 1 接入

### 1.1 添加依赖
**1.在`Project(build.gradle)`中：**
```
buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.3'
        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.1'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
```
**2.在`App(build.gradle)`中：**
```
apply plugin: 'com.android.application'
apply plugin: 'org.greenrobot.greendao'

android {
    compileSdkVersion 25
    buildToolsVersion "25.0.2"
    defaultConfig {
        //...
    }
    buildTypes {
        //...
    }
    greendao{
        schemaVersion 1//数据库版本号，从1开始
        daoPackage 'com.ruicb.greendaotest.database'//自动生成DaoMaster、DaoSession的目录
        targetGenDir 'src/main/java'//目标目录
    }
}

dependencies {
    //添加对greenDao的依赖
    compile 'org.greenrobot:greendao:3.2.0'
}
```
3.同步一下项目，将依赖拉取到本地。

### 1.2 激活GreenDao自动生成管理类和会话类。

新增一个bean对象，只写属性即可，如下：
```
@Entity
public class Face {
    @Id
    Long id;
    @NotNull
    String localPath;
}
```
build一下项目，greenDao会在之前指定的目录下，自动生成管理类DaoMaster和会话类DaoSession，同时会自动为`Face`类完善构造方法和getter、setter方法，以及生成一个对应的`FaceDao`类。

### 1.3 初始化GreenDao
在自定义Application中初始化GreenDao，并对外暴露DaoSession，作为后期对数据库进行操作的入口：
```
public class App extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
        * context 上下文
        * name 要创建的数据库名称
        */
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "magic-sticker-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    //对外暴露DaoSession
    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
```

## 2 使用

依赖并初始化完毕，下面就是Api的使用，不再赘述。比如向数据库中插入`Face`对象：
```
//得到会话
DaoSession session = App.getDaoSession();
Face face = new Face();
session.getFaceDao().insert(face);
```

## 3 更新

### 3.1 GreenDao自身更新的问题

GreenDao自带的更新类`DevOpenHelper`，是不可用的，如下：
```
/** WARNING: Drops all table on Upgrade! Use only during development. */
public static class DevOpenHelper extends OpenHelper {
    public DevOpenHelper(Context context, String name) {
        super(context, name);
    }
    public DevOpenHelper(Context context, String name, CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        dropAllTables(db, true);
        onCreate(db);
    }
}
```
注释明确说明了仅限于开发阶段，并不适合上线后再更新数据库。在11、12行，GreenDao在数据库版本更新时，默认删除所有表再新建，这对于线上app来说是致命的。

可能我们会想，那我们改掉它不就行了吗？改是不行的，因为这个类是GreenDao自己生成的，即使改了也会在下次build时被覆盖，没用的。

### 3.2 自定义更新

既然如此，我们就需要有自己的更新类继承自`OpenHelper`，重写`onUpgrade()`方法以自己维护更新：
```
public class MyOpenHelper extends DaoMaster.OpenHelper {

    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        //写自己的更新逻辑
}
```
此时，就可以在14行的位置写自己的逻辑了。

同时，要让GreenDao知道我们使用自定义的更新类，所以修改Application中初始化得操作：
```
public class App extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        //使用自定义类
        MyOpenHelper helper = new MyOpenHelper(this, "magic-sticker-db");
        //...
    }
    //...
}
```

下面开始真正的更新操作，分别以`新建表`和`更新已有表的字段`为例。

### 3.3 新建表

新建表是比较简单的，大致步骤如下：
1.新建bean对象，build一下项目，会生成对应的XxxDao；
2.修改`app:build.gradle中`的版本号，加1；
3.在`MyOpenHelper`的`onUpgrade()`方法中创建新表：
```
XxxDao.createTable(db, false);
```
4.运行即可；

### 3.4 更新已有表的字段

相比于新建，新增字段比较复杂，以在`Face`为例，分为三步：
1. 备份Face表到Face_Temp；
2. 删除Face表；
3. 新建Fce表；
4. 迁移Face_Temp的数据至Face；

我们有一个开源的辅助类，如下：
```
public class MigrationHelper {

    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION =
        "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";
    private static MigrationHelper instance;

    public static MigrationHelper getInstance() {
        if (instance == null) {
            instance = new MigrationHelper();
        }
        return instance;
    }

    public void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        //备份
        generateTempTables(db, daoClasses);
        //删除
        DaoMaster.dropAllTables(db, true);
        //重新创建
        DaoMaster.createAllTables(db, false);
        //恢复数据
        restoreData(db, daoClasses);
    }

    /**
     * 备份要更新的表
     */
    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

            String divider = "";
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList<>();

            StringBuilder createTableStringBuilder = new StringBuilder();

            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");

            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if (getColumns(db, tableName).contains(columnName)) {
                    properties.add(columnName);

                    String type = null;

                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                    } catch (Exception exception) {
                    }

                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);

                    if (daoConfig.properties[j].primaryKey) {
                        createTableStringBuilder.append(" PRIMARY KEY");
                    }

                    divider = ",";
                }
            }
            createTableStringBuilder.append(");");

            db.execSQL(createTableStringBuilder.toString());

            StringBuilder insertTableStringBuilder = new StringBuilder();

            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tableName).append(";");

            db.execSQL(insertTableStringBuilder.toString());
        }
    }

    /**
     * 恢复数据
     */
    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();

            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                }
            }

            StringBuilder insertTableStringBuilder = new StringBuilder();

            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");

            StringBuilder dropTableStringBuilder = new StringBuilder();

            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);

            db.execSQL(insertTableStringBuilder.toString());
            db.execSQL(dropTableStringBuilder.toString());
        }
    }

    private String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
            return "INTEGER";
        }
        if (type.equals(Boolean.class)) {
            return "BOOLEAN";
        }

        Exception exception =
            new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
        throw exception;
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return columns;
    }
}
```

这样，只需要在更新字段时，在`MyOpenHelper`类的`onUpgrade()`方法中：
```
//更新字段
MigrationHelper.getInstance().migrate(db, FaceDao.class);
```

但是这样有问题，发现其每次都是删除所有表、再新建所有表，意思就是：每次我要更新一张表中的某个字段，我都需要传入所有的表对应的XxxDao.class对象，即使其他的不需要更新，也会经历`备份`、`删除`、`新建`、`恢复`的过程，效率极其低下。如果你不传所有，只传当前需要更新的表，则其他表的数据都会丢失。

于是我改造了一下，只需要传入你想更新的表对应的类的class对象即可：
```
public class MigrationHelper {

    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION =
        "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";
    private static MigrationHelper instance;

    public static MigrationHelper getInstance() {
        if (instance == null) {
            instance = new MigrationHelper();
        }
        return instance;
    }

    public void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        //备份
        generateTempTables(db, daoClasses);
        //只删除需要更新的表
        deleteOriginalTables(db, daoClasses);
        //只创建需要更新的表
        //DaoMaster.createAllTables(db, false);
        createOrignalTables(db, daoClasses);
        //恢复数据
        restoreData(db, daoClasses);
    }

    /**
     * 备份要更新的表
     */
    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        //...
    }

    /**
     * 通过反射，删除要更新的表
     */
    private void deleteOriginalTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses){
        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
            try {
                Method method = daoClass.getMethod("dropTable", Database.class, boolean.class);
                method.invoke(null, db, true);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过反射，重新创建要更新的表
     */
    private void createOrignalTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses){
        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
            try {
                Method method = daoClass.getMethod("createTable", Database.class, boolean.class);
                method.invoke(null, db, false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 恢复数据
     */
    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        //...
    }

    private String getTypeByClass(Class<?> type) throws Exception {
        //...
    }

    private static List<String> getColumns(Database db, String tableName) {
        //...
    }
}
```

至此，有关GreenDao依赖、初始化、使用、更新全部介绍完了。

## 4 跨版本升级
升级数据库时，我们要考虑跨版本更新的用户，所以一般在`MyOpenHelper`的`onUpgrade()`的方法中，会这么写：
```
@Override
public void onUpgrade(Database db, int oldVersion, int newVersion) {
    super.onUpgrade(db, oldVersion, newVersion);
    switch (oldVersion){
        case 1:
            //更新字段
            MigrationHelper.getInstance().migrate(db, FaceDao.class);
        case 2:
        case 3:
        case 4:
            //新增表
            StickerDao.createTable(db, false);
        case 5:
            //更新字段
            MigrationHelper.getInstance().migrate(db, StickerDao.class);
        case 6:
            //更新字段
            MigrationHelper.getInstance().migrate(db, StickerDao.class);
        case 7:
            //新增表
            StickerBackgroundDao.createTable(db, false);
            StickerCategoryDao.createTable(db, false);
            //更新字段
            MigrationHelper.getInstance().migrate(db, StickerDao.class, FaceDao.class);
        case 8:
            //更新字段
            MigrationHelper.getInstance().migrate(db, StickerBackgroundDao.class);
        case 9:
            //更新字段
            MigrationHelper.getInstance().migrate(db, StickerBackgroundDao.class);
            //更新字段
            MigrationHelper.getInstance().migrate(db, StickerCategoryDao.class);
            //新增表
            StickerTextDao.createTable(db, false);
        case 10:
        case 11:
            //新增表
            StickerFontDao.createTable(db, false);
    }
}
```