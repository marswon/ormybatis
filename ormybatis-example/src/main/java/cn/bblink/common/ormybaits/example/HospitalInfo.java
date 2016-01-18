package cn.bblink.common.ormybaits.example;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;

@Data
public class HospitalInfo implements Serializable {

	Long id;
	String hospitalName;
	String hospitalTelephone;
	String hospitalAddress;
	String hospitalWebsite;
	String hospitalLevel;
	String hospitalCategory;
	String hospitalLocate;
	String hospitalDesc;
	String hospitalTraffic;
	String hospitalPark;
	String hospitalAround;
	String province;
	String city;
	String area;
	Long createUserId;
	Date createTime;
	Long modifyUserId;
	Date modifyTime;
	String remark;
	String hospitalFeature;
	String hospitalLogoPath;
	Long hospitalOutpatient;
	Long hospitalChildbirth;
	String hospitalWeibo;
	String hospitalWeixin;
	String hospitalCsPhone;
	String hospitalComplainPhone;
	Date openingTime;
	String hospitalNewLev;
	String hospitalAbbre;
	Long hospitalFlowerNum;
	Long hospitalPatientNum;
	Long visitNum;
	String hosStatus;
	
	public final static String
		F_HOSPITAL_ID = "hospital_id",
		F_HOSPITAL_NAME = "hospital_name",
		F_HOSPITAL_TELEPHONE = "hospital_telephone",
		F_HOSPITAL_ADDRESS = "hospital_address",
		F_HOSPITAL_WEBSITE = "hospital_website",
		F_HOSPITAL_LEVEL = "hospital_level",
		F_HOSPITAL_CATEGORY = "hospital_category",
		F_HOSPITAL_LOCATE = "hospital_locate",
		F_HOSPITAL_DESC = "hospital_desc",
		F_HOSPITAL_TRAFFIC = "hospital_traffic",
		F_HOSPITAL_PARK = "hospital_park",
		F_HOSPITAL_AROUND = "hospital_around",
		F_PROVINCE = "province",
		F_CITY = "city",
		F_AREA = "area",
		F_CREATE_USER_ID = "create_user_id",
		F_CREATE_TIME = "create_time",
		F_MODIFY_USER_ID = "modify_user_id",
		F_MODIFY_TIME = "modify_time",
		F_REMARK = "remark",
		F_HOSPITAL_FEATURE = "hospital_feature",
		F_HOSPITAL_LOGO_PATH = "hospital_logo_path",
		F_HOSPITAL_OUTPATIENT = "hospital_outpatient",
		F_HOSPITAL_CHILDBIRTH = "hospital_childbirth",
		F_HOSPITAL_WEIBO = "hospital_weibo",
		F_HOSPITAL_WEIXIN = "hospital_weixin",
		F_HOSPITAL_CS_PHONE = "hospital_cs_phone",
		F_HOSPITAL_COMPLAIN_PHONE = "hospital_complain_phone",
		F_OPENING_TIME = "opening_time",
		F_HOSPITAL_NEW_LEV = "hospital_new_lev",
		F_HOSPITAL_ABBRE = "hospital_abbre",
		F_HOSPITAL_FLOWER_NUM = "hospital_flower_num",
		F_HOSPITAL_PATIENT_NUM = "hospital_patient_num",
		F_VISIT_NUM = "visit_num",
		F_HOS_STATUS = "hos_status";
	
	public final static String DB_TABLE_NAME = "bblink_hospital_info";
}