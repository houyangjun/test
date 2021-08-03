package com.jun.alyunsms;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {

	public static void sendShortMessage(String phoneNumbers,String param) throws ClientException {

		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4G8BAD5rsnPwdmoe6TQK", "mqTPV0Wmpd2Hyz9rfecxpA0MkjDe6d");

		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest();

		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("PhoneNumbers", phoneNumbers);
		request.putQueryParameter("SignName", "快速指定文件夹清理");
		request.putQueryParameter("TemplateCode", "SMS_168825399");
		request.putQueryParameter("TemplateParam", "{\"code\":\""+param+"\"}");

		System.out.println(phoneNumbers+"-- - - -phoneNumbers");
		System.out.println(param+"-- - - -param");
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}


	}


}
