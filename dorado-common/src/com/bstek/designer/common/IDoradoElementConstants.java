package com.bstek.designer.common;

/**
 * @author Robin
 */
public interface IDoradoElementConstants {

    public final static String VIEW = "View";

    public final static String VIEW_CONFIG = "ViewConfig";

    public final static String ARGUMENTS = "Arguments";

    public final static String ARGUMENT = "Argument";

    public final static String HTML_CONTAINER = "HtmlContainer";

    public final static String BUTTON = "Button";

    public final static String PANEL = "Panel";

    public final static String CARD_BOOK = "CardBook";

    public final static String CHECK_BOX = "CheckBox";

    public final static String DATA_GRID = "DataGrid";

    public final static String DATA_TREE_GRID = "DataTreeGrid";

    public final static String DATA_LABEL = "DataLabel";

    public final static String DATA_LIST_BOX = "DataListBox";

    public final static String DATA_TEXT_EDITOR = "DataTextEditor";

    public final static String DATA_TREE = "DataTree";

    public final static String GRID = "Grid";

    public final static String IFRAME = "IFrame";

    public final static String LIST_BOX = "ListBox";

    public final static String MENU = "Menu";

    public final static String RADIO_GROUP = "RadioGroup";

    public final static String TAB_BAR = "TabBar";

    public final static String TAB_CONTROL = "TabControl";

    public final static String TEXT_EDITOR = "TextEditor";

    public final static String TREE = "Tree";

    public final static String COLUMNS = "Columns";

    public final static String DATA_COLUMN = "DataColumn";

    public final static String RADIO_BUTTONS = "RadioButtons";

    public final static String RADIO_BUTTON = "RadioButton";

    public final static String MENU_ITEM = "MenuItem";

    public final static String MENU_ITEMS = "MenuItems";

    public final static String SEPERATOR = "Seperator";

    public final static String TAB_CONTROL_TABS = "TabControlTabs";

    public final static String TAB_BAR_TABS = "TabBarTabs";

    public final static String TAB = "Tab";

    public final static String CONTROL_TAB = "ControlTab";

    public final static String IFRAME_TAB = "IFrameTab";

    public final static String BLOCK_VIEW = "BlockView";

    public final static String CONTROLS = "Controls";

    public final static String AUTO_FORM_ELEMENT = "AutoFormElement";

    public final static String AUTO_FORM = "AutoForm";

    public final static String TABLE = "Table";

    public final static String SQL_TABLE = "SqlTable";

    public final static String KEY_COLUMN = "KeyColumn";

    // ----------------------------------------------------
    public final static String MODEL = "Model";

    public final static String DATA_SET = "DataSet";

    public final static String DATA_OBJECT = "DataObject";

    public final static String ABSTRACT_DATA_TYPE = "AbstractDataType";

    public final static String DATA_TYPE = "DataType";

    public final static String ABSTRACT_DATA_PROVIDER = "AbstractDataProvider";

    public final static String DATA_PROVIDER = "DataProvider";

    public final static String ABSTRACT_DATA_RESOLVER = "AbstractDataResolver";

    public final static String DATA_RESOLVER = "DataResolver";

    public final static String DIRECT_DATA_RESOLVER = "DirectDataResolver";

    public final static String DIRECT_DATA_PROVIDER = "DirectDataProvider";

    public final static String HIBERNATE_HQL_DATA_PROVIDER = "HibernateHqlDataProvider";

    public final static String PROPERTY_SUPPORT = "PropertyDefSupport";

    public final static String PROPERTY_DEF = "PropertyDef";

    public final static String BASE_PROPERTY_DEF = "BasePropertyDef";

    public final static String REFERENCE = "Reference";

    public final static String LOOKUP = "Lookup";

    public final static String VALIDATOR = "Validator";

    public final static String CONSTRAINT = "Constraint";

    public final static String PROPERTY_DATA_TYPE = "PropertyDefDataType";

    public final static String INNER_DATA_PROVIDER = "InnerDataProvider";

    public final static String PROPERTY = "Property";

    public final static String CLIENT_EVENT = "ClientEvent";

    public final static String ENTITY = "Entity";

    public final static String COLLECTION = "Collection";

    public final static String GRAPH_META = "meta";

    public final static String DB_MODEL = "DbModel";

    public final static String AUTO_TABLE = "AutoTable";

    public final static String FROM_TABLES = "FromTables";

    public final static String JOIN_TABLES = "JoinTables";

    public final static String FROM_TABLE = "FromTable";

    public final static String JOIN_TABLE = "JoinTable";

    public final static String COLUMN = "Column";

    public final static String ORDERS = "Orders";

    public final static String ORDER = "Order";

    public final static String WHERE = "@Where";

    public final static String BETWEEN = "BetweenMatchRule";

    public final static String RULE = "SimpleMatchRule";

    public final static String SQL = "SqlMatchRule";

    public final static String JUNCTION = "JunctionMatchRule";

    public final static String AUTO_TABLE_COLUMN = "AutoTableColumn";

    public final static String[] ABSTRACT_DATA_OBJECTS = new String[]{
            ABSTRACT_DATA_TYPE, ABSTRACT_DATA_PROVIDER, ABSTRACT_DATA_RESOLVER};

    public final static String[] BASE_DATA_OBJECTS = new String[]{
            DATA_PROVIDER, DATA_RESOLVER};

    public final static String[] ALL_MODEL_ELEMENTS = new String[]{DATA_SET,
            DATA_TYPE, DIRECT_DATA_PROVIDER, DATA_PROVIDER, DATA_RESOLVER,
            PROPERTY_DEF, REFERENCE, LOOKUP, VALIDATOR, CONSTRAINT};

    public final String[] ROOT_ELEMENTS = new String[]{MODEL, VIEW_CONFIG,
            VIEW, ARGUMENTS, DB_MODEL};

    public final String[] NO_AUXILIARY_ROOT_ELEMENTS = new String[]{MODEL,
            VIEW_CONFIG, ARGUMENTS};

    public final String[] TABLE_COLUMN_ELEMENTS = new String[]{"TableColumn",
            "TableKeyColumn", "SqlTableColumn", "AutoTableColumn"};

    public static String[] PROPERTY_DEFINATION_ELEMENTS = new String[]{
            DATA_TYPE, DIRECT_DATA_PROVIDER};

    public final static String[] DATATYPE_ELEMENTS = new String[]{
            BASE_PROPERTY_DEF, LOOKUP, REFERENCE};

    public final String[] INNER_MODEL_ELEEMNTS = new String[]{
            "ResultDataType", "ParameterDataType", "PropertyDefDataType"};

    public final String[] AUXILIARY_ELEMNTS = new String[]{"Import",
            "ExportStart", "ExportEnd", "ImportStart", "ImportEnd"};

    public final String[] VALIDATOR_ELEMENTS = new String[]{"EnumValidator",
            "LengthValidator", "RegExpValidator", "RangeValidator",
            "AjaxValidator"};

    public final String[] PROPERTY_ELEMENTS = new String[]{PROPERTY,
            CLIENT_EVENT};

    public static String[] AUXILIARY_ELEMENTS = {"Import", "GroupStart",
            "GroupEnd", "PlaceHolder", "PlaceHolderStart", "PlaceHolderEnd"};

    public final String[] ENTITY_ELEEMNTS = new String[]{"Entity",
            "Collection"};

    public final String[] SIMPLE_ENTITY_PROPERTY_ELEMENTS = new String[]{"properties"};

    public final String[] CLASS_TYPE_PROPERTY = new String[]{"matchType",
            "impl", "creationType"};

    public final String[] COLUMNS_ELEMENTS = new String[]{"Column",
            "TableKeyColumn", "SqlTableColumn", "AutoTableColumn"};

    public final String[] TABLE_ELEMENTS = new String[]{"Table", "SqlTable",
            "AutoTable"};
}