package top.bootz.old_security.api.common;

import org.springframework.http.MediaType;

// TODO: Auto-generated Javadoc
/**
 * The Class Constants.
 */
public class APIConstants {
	/**
	 * The Constant MEDIATYPE.
	 */
	public static final String MEDIATYPE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

	/**
	 * The Constant HEADER_NAME_TOKEN.
	 */
	public static final String HEADER_NAME_TOKEN = "Token";

	/**
	 * The Constant HEADER_NAME_SOURCE.
	 */
	public static final String HEADER_NAME_SOURCE = "Source";

	/**
	 * The Constant HEADER_NAME_LOCATION.
	 */
	public static final String HEADER_NAME_LOCATION = "Location";

	/**
	 * The Constant HEADER_NAME_USERAGENT.
	 */
	public static final String HEADER_NAME_USERAGENT = "user-agent";

	/**
	 * The Constant LOG_TYPE_GETSINGLE.
	 */
	public static final int LOG_TYPE_GETSINGLE = 1;

	/**
	 * The Constant LOG_TYPE_GETLIST.
	 */
	public static final int LOG_TYPE_GETLIST = 2;

	/**
	 * The Constant LOG_TYPE_CREATESINGLE.
	 */
	public static final int LOG_TYPE_CREATESINGLE = 3;

	/**
	 * The Constant LOG_TYPE_CREATEMULTIPLE.
	 */
	public static final int LOG_TYPE_CREATEMULTIPLE = 4;

	/**
	 * The Constant LOG_TYPE_UPDATESINGLE.
	 */
	public static final int LOG_TYPE_UPDATESINGLE = 5;

	/**
	 * The Constant LOG_TYPE_DELETESINGLE.
	 */
	public static final int LOG_TYPE_DELETESINGLE = 6;

	/**
	 * The Constant LOG_TYPE_SEARCH.
	 */
	public static final int LOG_TYPE_SEARCH = 7;

	/**
	 * The Constant LOG_TYPE_PUBLISH.
	 */
	public static final int LOG_TYPE_PUBLISH = 8;

	/**
	 * The Constant LOG_TYPE_LOGIN.
	 */
	public static final int LOG_TYPE_LOGIN = 9;

	/**
	 * The Constant LOG_TYPE_DELETEMULTIPLE.
	 */
	public static final int LOG_TYPE_DELETEMULTIPLE = 11;

	/**
	 * The Constant LOG_TYPE_UPDATEMULTIPLE.
	 */
	public static final int LOG_TYPE_UPDATEMULTIPLE = 12;

	/** The Constant LOG_TYPE_CLONE. */
	public static final int LOG_TYPE_CLONE = 13;

	/** The Constant LOG_TYPE_CLONE. */
	public static final int LOG_TYPE_ORDER = 14;

	/** The Constant LOG_TYPE_FOURD. */
	public static final int LOG_TYPE_FOURD = 9999;

	/**
	 * The Constant LOG_OBJ_AREA.
	 */
	public static final String LOG_OBJ_AREA = "area";

	/**
	 * The Constant LOG_OBJ_CAPTCHA.
	 */
	public static final String LOG_OBJ_CAPTCHA = "captcha";

	/**
	 * The Constant LOG_OBJ_ORG.
	 */
	public static final String LOG_OBJ_ORG = "org";
	
	/** The Constant LOG_OBJ_EXAM. */
	public static final String LOG_OBJ_EXAM = "exam";

	/** The Constant LOG_OBJ_EXAMPAPER. */
	public static final String LOG_OBJ_EXAMPAPER = "exampaper";
	
	/**
	 * The Constant LOG_OBJ_ORGUSER.
	 */
	public static final String LOG_OBJ_ORGUSER = "orguser";

	/**
	 * The Constant LOG_OBJ_CONSUMERUSER.
	 */
	public static final String LOG_OBJ_CONSUMERUSER = "consumeruser";

	/**
	 * The Constant LOG_OBJ_GLOBAL.
	 */
	public static final String LOG_OBJ_GLOBAL = "global";

	/**
	 * The Constant LOG_OBJ_LASTLOGINTIME.
	 */
	public static final String LOG_OBJ_LASTLOGINTIME = "lastlogintime";

	/** The Constant LOG_OBJ_JINMUBIAO. */
	public static final String LOG_OBJ_JINGOAL = "jingoal";
	
	public static final String LOG_OBJ_DAOGOUNIU = "daogouniu";

	/**
	 * The Constant LOG_OBJ_KNOWLEDGE.
	 */
	public static final String LOG_OBJ_KNOWLEDGE = "knowledge";

	/**
	 * The Constant LOG_OBJ_PENDINGKNOWLEDGE.
	 */
	public static final String LOG_OBJ_PENDINGKNOWLEDGE = "pendingKnowledge";

	/**
	 * The Constant LOG_OBJ_PRETRANSKNOWLEDGE.
	 */
	public static final String LOG_OBJ_PRETRANSKNOWLEDGE = "preTransKnowledge";

	/**
	 * The Constant LOG_OBJ_KNOWLEDGESTATUS.
	 */
	public static final String LOG_OBJ_KNOWLEDGESTATUS = "knowledgeStatus";

	/**
	 * The Constant LOG_OBJ_PUBLICKNOWLEDGE.
	 */
	public static final String LOG_OBJ_PUBLICKNOWLEDGE = "publicKnowledge";

	/**
	 * The Constant LOG_OBJ_USERKNOWLEDGE.
	 */
	public static final String LOG_OBJ_USERKNOWLEDGE = "userKnowledge";

	/**
	 * The Constant LOG_OBJ_ALLSYSTEMTAGS.
	 */
	public static final String LOG_OBJ_ALLSYSTEMTAGS = "publicTag";

	/**
	 * The Constant LOG_OBJ_USERTAGS.
	 */
	public static final String LOG_OBJ_USERTAGS = "userTag";

	/**
	 * The Constant LOG_OBJ_TRANSDATA.
	 */
	public static final String LOG_OBJ_TRANSDATA = "transData";

	/**
	 * The Constant LOG_OBJ_ORGUSER_ROLE.
	 */
	public static final String LOG_OBJ_ORGUSER_ROLE = "orguserRole";

	/**
	 * The Constant LOG_OBJ_MENUS.
	 */
	public static final String LOG_OBJ_MENUS = "menus";

	/**
	 * The Constant LOG_OBJ_PERMISSION.
	 */
	public static final String LOG_OBJ_PERMISSION = "permission";

	/**
	 * The Constant LOG_OBJ_GROUP.
	 */
	public static final String LOG_OBJ_GROUP = "group";

	/**
	 * The Constant LOG_OBJ_PRODUCTAPPLY.
	 */
	public static final String LOG_OBJ_PRODUCTAPPLY = "productapply";

	/**
	 * The Constant LOG_OBJ_GROUPMEMBER.
	 */
	public static final String LOG_OBJ_GROUPMEMBER = "groupMember";

	/**
	 * The Constant LOG_OBJ_POSITION.
	 */
	public static final String LOG_OBJ_POSITION = "position";

	/** The Constant LOG_OBJ_FOURD. */
	public static final String LOG_OBJ_FOURD = "fourd";

	/**
	 * The Constant LOG_OBJ_ORGPOSITION.
	 */
	public static final String LOG_OBJ_ORGPOSITION = "orgposition";

	/**
	 * The Constant LOG_OBJ_ORGFUNCTION.
	 */
	public static final String LOG_OBJ_ORGFUNCTION = "orgfunction";

	/** The Constant LOG_OBJ_QUESTION. */
	public static final String LOG_OBJ_QUESTION = "question";

	/**
	 * The Constant LOG_OBJ_BEHAVIOR.
	 */
	public static final String LOG_OBJ_BEHAVIOR = "behavior";

	/**
	 * The Constant LOG_OBJ_INVITATION.
	 */
	public static final String LOG_OBJ_INVITATION = "invitation";

	/**
	 * The Constant LOG_OBJ_MESSAGE.
	 */
	public static final String LOG_OBJ_MESSAGE = "message";

	/**
	 * The Constant LOG_OBJ_PASSWORD.
	 */
	public static final String LOG_OBJ_PASSWORD = "password";

	/**
	 * The Constant LOG_OBJ_SKILL.
	 */
	public static final String LOG_OBJ_SKILL = "skill";

	/**
	 * The Constant LOG_OBJ_INDUSTRY.
	 */
	public static final String LOG_OBJ_INDUSTRY = "industry";
	
	public static final String LOG_OBJ_ZO = "zo";

	/**
	 * The Constant LOG_OBJ_ORDER.
	 */
	public static final String LOG_OBJ_ORDER = "order";

	/**
	 * The Constant LOG_OBJ_PRODUCT.
	 */
	public static final String LOG_OBJ_PRODUCT = "product";

	/**
	 * The Constant LOG_OBJ_PRODUCT_CART.
	 */
	public static final String LOG_OBJ_PRODUCT_CART = "productcart";

	/** The Constant LOG_OBJ_USERCONFIG. */
	public static final String LOG_OBJ_USERCONFIG = "userconfig";

	/**
	 * The Constant LOG_OBJ_ORGACTIVITIY.
	 */
	public static final String LOG_OBJ_ORGACTIVITIY = "orgActivitiy";

	/**
	 * The Constant LOG_OBJ_USERACTIVITIY.
	 */
	public static final String LOG_OBJ_USERACTIVITIY = "userActivitiy";

	/**
	 * The Constant LOG_OBJ_USER_CAREER.
	 */
	public static final String LOG_OBJ_USER_CAREER = "userCareer";

	/**
	 * The Constant LOG_OBJ_USER_EDUCATION.
	 */
	public static final String LOG_OBJ_USER_EDUCATION = "userEducation";

	/**
	 * The Constant LOG_OBJ_COMMENT.
	 */
	public static final String LOG_OBJ_COMMENT = "comment";

	/**
	 * The Constant LOG_OBJ_PRAISE.
	 */
	public static final String LOG_OBJ_PRAISE = "praise";

	/**
	 * The Constant LOG_OBJ_RATING.
	 */
	public static final String LOG_OBJ_RATING = "rating";

	/**
	 * The Constant LOG_OBJ_FAVORITE.
	 */
	public static final String LOG_OBJ_FAVORITE = "favorite";

	/**
	 * The Constant LOG_OBJ_NOTE.
	 */
	public static final String LOG_OBJ_NOTE = "note";

	/**
	 * The Constant LOG_OBJ_NOTE_COUNT.
	 */
	public static final String LOG_OBJ_NOTE_COUNT = "noteCount";

	/**
	 * The Constant LOG_OBJ_NOTE_SHARED_USERS.
	 */
	public static final String LOG_OBJ_NOTE_SHARED_USERS = "noteSharedUsers";

	/**
	 * The Constant LOG_OBJ_MYNOTE.
	 */
	public static final String LOG_OBJ_MYNOTE = "mynote";

	/**
	 * The Constant LOG_OBJ_SHAREDNOTE.
	 */
	public static final String LOG_OBJ_SHAREDNOTE = "sharednote";

	/**
	 * The Constant LOG_OBJ_MYKNGNOTE.
	 */
	public static final String LOG_OBJ_MYKNGNOTE = "mykngnote";

	/**
	 * The Constant LOG_OBJ_SHAREDKNGNOTE.
	 */
	public static final String LOG_OBJ_SHAREDKNGNOTE = "sharedkngnote";

	/**
	 * The Constant LOG_OBJ_BROWSEHISTORY.
	 */
	public static final String LOG_OBJ_BROWSEHISTORY = "browsehistory";

	/**
	 * The Constant LOG_OBJ_DEPARTMENT.
	 */
	public static final String LOG_OBJ_DEPARTMENT = "department";

	/**
	 * The Constant LOG_OBJ_SUBDEPARTMENT.
	 */
	public static final String LOG_OBJ_SUBDEPARTMENT = "subDepartment";

	/**
	 * The Constant LOG_OBJ_DEPARTMENTMEMBER.
	 */
	public static final String LOG_OBJ_DEPARTMENTMEMBER = "departmentMember";

	/**
	 * The Constant LOG_OBJ_ORGPOSITION.
	 */
	public static final String LOG_OBJ_DEPARTMENTPOSITION = "departmentPosition";

	/**
	 * The Constant LOG_OBJ_FILEINFO.
	 */
	public static final String LOG_OBJ_FILEINFO = "fileinfo";

	/**
	 * The Constant LOG_OBJ_SELFSTUDY.
	 */
	public static final String LOG_OBJ_SELFSTUDY = "selfstudy";

	/**
	 * The Constant LOG_OBJ_TAG.
	 */
	public static final String LOG_OBJ_TAG = "tag";

	/** The Constant LOG_OBJ_TOKEN. */
	public static final String LOG_OBJ_TOKEN = "token";

	/**
	 * The Constant LOG_OBJ_STUDY_PLAN.
	 */
	public static final String LOG_OBJ_STUDY_PLAN = "studyplan";

	/**
	 * The Constant LOG_OBJ_STUDY_PLAN_USERS.
	 */
	public static final String LOG_OBJ_STUDY_PLAN_USERS = "studyplanUsers";

	/**
	 * The Constant LOG_OBJ_ALL_STUDY_PLAN.
	 */
	public static final String LOG_OBJ_ALL_STUDY_PLAN = "allStudyplan";

	/**
	 * The Constant LOG_OBJ_STUDY_PLAN_COUNT.
	 */
	public static final String LOG_OBJ_STUDY_PLAN_COUNT = "studyplanCount";

	/** The Constant LOG_OBJ_STUDY_PLAN_EXECUTOR. */
	public static final String LOG_OBJ_STUDY_PLAN_EXECUTOR = "studyplanExecutor";

	/**
	 * The Constant LOG_OBJ_USER_STUDY_PLAN.
	 */
	public static final String LOG_OBJ_USER_STUDY_PLAN = "userstudyplan";

	/**
	 * The Constant LOG_OBJ_KNOWLEDGE_STUDYING_USERS.
	 */
	public static final String LOG_OBJ_KNOWLEDGE_STUDYING_USERS = "knowledgeStudyingUsers";

	/**
	 * The Constant LOG_OBJ_ALL_USER_STUDY_PLAN.
	 */
	public static final String LOG_OBJ_ALL_USER_STUDY_PLAN = "allUserstudyplan";

	/**
	 * The Constant LOG_OBJ_STUDY_PLAN_CONTENT.
	 */
	public static final String LOG_OBJ_STUDY_PLAN_CONTENT = "studyplanContent";

	/**
	 * The Constant LOG_OBJ_PRODUCTCART.
	 */
	public static final String LOG_OBJ_PRODUCTCART = "productcart";

	/**
	 * The Constant LOG_OBJ_NEWS.
	 */
	public static final String LOG_OBJ_NEWS = "news";

	/**
	 * The Constant LOG_OBJ_WEBINAR.
	 */
	public static final String LOG_OBJ_WEBINAR = "webinar";

	/** The Constant LOG_OBJ_DICT. */
	public static final String LOG_OBJ_DICT = "dict";

	/** The Constant LOG_OBJ_DICT. */
	public static final String LOG_OBJ_ENTERPRISE = "enterprise";

	/**
	 * The Constant LOG_OBJ_WEBINAR.
	 */
	public static final String LOG_OBJ_APPVERSION = "appVersion";

	/**
     * The Constant LOG_OBJ_APPAD.
     */
    public static final String LOG_OBJ_APPAD = "appAd";
    
    /**
	 * The Constant LOG_OBJ_FOLDER.
	 */
	public static final String LOG_OBJ_FOLDER = "folder";
	
	public static final String LOG_OBJ_CONVERTITEM = "convertItem";
	
	/**
	 * The Constant DEFAULT_LIMIT.
	 */
	public static final int DEFAULT_LIMIT = 20;

	/**
	 * The Constant PARAM_NAME_OFFSET.
	 */
	public static final String PARAM_NAME_OFFSET = "offset";

	/**
	 * The Constant PARAM_NAME_LIMIT.
	 */
	public static final String PARAM_NAME_LIMIT = "limit";

	/**
	 * The Constant PARAM_NAME_ORDERBY.
	 */
	public static final String PARAM_NAME_ORDERBY = "orderby";

	/**
	 * The Constant PARAM_NAME_DIRECTION.
	 */
	public static final String PARAM_NAME_DIRECTION = "direction";

	/**
	 * The Constant PARAM_NAME_BROWSETYPE.
	 */
	public static final String PARAM_NAME_BROWSETYPE = "browsetype";

	/**
	 * The Constant PARAM_NAME_TYPE.
	 */
	public static final String PARAM_NAME_TYPE = "type";

	/**
	 * The Constant ORDER_CREATE_STATUS.
	 */
	public static final int ORDER_CREATE_STATUS = 0;
	/**
	 * The Constant FEEDBACK_CREATE_STATUS.
	 */
	public static final int FEEDBACK_CREATE_STATUS = 3;

	/**
	 * The Constant ORDER_CREATE_STATUS.
	 */
	public static final String FILE_TYPE_DOC = "doc";

	/**
	 * The Constant Token for Server Call Service.
	 */
	public static final String PERMISSION_BACKEND_TOKEN = "AAAAAPVyJybpRYPathkwUsgw_352lAUH3SM5sR4G5oJmYLLKEcprqun3wjMTDlInuoJdanXsD6BX3djQTET7GlI9BoRozmjQFr1CgVVXXKZff-udkF4fpCG_7hZuue7J3n49OQu4yUNqwZbKIhvkW-zJLYw";

	/** The Constant LOG_OBJ_TOPIC. */
	public static final String LOG_OBJ_TOPIC = "topic";

	/**
	 * The Constant LOG_OFFLINECOURSE.
	 */
	public static final String LOG_OFFLINECOURSE = "offlineCourse";
	/**
	 * The Constant LOG_COURSEREGIST.
	 */
	public static final String LOG_COURSEREGIST = "courseRegist";
	/**
	 * The Constant USER_CONFIG_INIT.
	 */
	public static final String USER_CONFIG_INIT = "InitSetting";

	/** The Constant LOG_OBJ_DRYCARGO. */
	public static final String LOG_OBJ_DRYCARGO = "drycargo";

	/** The Constant LOG_OBJ_GROUPSHARING. */
	public static final String LOG_OBJ_GROUPSHARING = "groupsharing";

	/** The Constant LOG_OBJ_HOTSPOT. */
	public static final String LOG_OBJ_HOTSPOT = "hotspot";

	/** The Constant LOG_OBJ_GROUP_ACTIVITY. */
	public static final String LOG_OBJ_GROUP_ACTIVITY = "groupactivity";

	/** The Constant LOG_OBJ_GROUP_COURSE. */
	public static final String LOG_OBJ_GROUP_COURSE = "groupcourse";

	/** The Constant LOG_OBJ_BOUGHT_KNOWLEDGE. */
	public static final String LOG_OBJ_BOUGHT_KNOWLEDGE = "boughtknowledge";

	public static final String LOG_OBJ_SOLUTION = "solution";

	/**
	 * The Constant LOG_OBJ_IMGROUP.
	 */
	public static final String LOG_OBJ_IMGROUP = "imgroup";

	/**
	 * The Constant LOG_OBJ_IMGROUPUSER.
	 */
	public static final String LOG_OBJ_IMGROUPUSER = "imgroupuser";

	/** The Constant ADMIN_EMAIL. */
	public static final String ADMIN_EMAIL = "liqs@yunxuetang.cn";

	public static final String LOG_OBJ_NOTICE = "notice";

	/** The Constant SHA256_SALT. */
	public static final String SHA256_SALT = "21a2aab97d020fabf92a0107b8f9a17d";
	
	public static final String SHA256_SALT_M3U8 = "2d2297de0b4810a71c136b8be5599c30";
	
	/**
	 * Hide Constructor.
	 */
	private APIConstants() {
	}
}
