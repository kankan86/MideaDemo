package meta.midea.data;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class HttpServer {
	private static final String TAG = "API";
	private static final String USER_AGENT = "Mozilla/4.5";

	public static String getGetRequest(String url, List<NameValuePair> params)
			throws Exception {
		String result = null;
		int statusCode = 0;

		HttpGet getMethod = new HttpGet(url);
		Log.e(TAG, "do the getRequest,url=" + url + "");

		try {
			getMethod.setHeader("User-Agent", USER_AGENT);
			// 添加用户密码验证信息
			// client.getCredentialsProvider().setCredentials(
			// new AuthScope(null, -1),
			// new UsernamePasswordCredentials(mUsername, mPassword));

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = httpClient.execute(getMethod);
			// HttpResponse httpResponse = client.execute(getMethod);
			// statusCode == 200 正常
			statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e(TAG, "statuscode = " + statusCode);
			// 处理返回的httpResponse信息
			result = retrieveInputStream(httpResponse.getEntity());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage() == null ? null : e.getMessage());
			result = "timeout";

			//throw new Exception(e);
			return result;
		} finally {
			getMethod.abort();
		}

		System.out.println("result===" + result);
		return result;
	}

	public static String getGetTestRequest(String url) throws Exception {
		String result = null;
		int statusCode = 0;

		//boolean tag = false;

		HttpGet getMethod = new HttpGet(url);
		Log.e(TAG, "do the getRequest,url=" + url + "");

		try {
			getMethod.setHeader("User-Agent", USER_AGENT);
			// 添加用户密码验证信息
			// client.getCredentialsProvider().setCredentials(
			// new AuthScope(null, -1),
			// new UsernamePasswordCredentials(mUsername, mPassword));

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 1200;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 1200;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = httpClient.execute(getMethod);
			// HttpResponse httpResponse = client.execute(getMethod);
			// statusCode == 200 正常
			statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e(TAG, "statuscode = " + statusCode);
			// 处理返回的httpResponse信息
			result = retrieveInputStream(httpResponse.getEntity());
//			if (statusCode == 200) {
//				tag = true;
//			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage() == null ? null : e.getMessage());
			result = "timeout";

			throw new Exception(e);
		} finally {
			getMethod.abort();
		}

		System.out.println("result===" + result);
		return result;
	}

	public static boolean getTestPostRequest(String url,
			List<NameValuePair> params, boolean isStrEntity) throws Exception {
		String result = null;
		int statusCode = 0;
		boolean tag = false;

		HttpPost getMethod = new HttpPost(url);
		if (params != null) {
			String paramstr = addParam(params);
			if (isStrEntity) {
				getMethod.setEntity(new StringEntity(paramstr));
			} else {
				getMethod
						.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
		}

		Log.e(TAG, "do the getRequest,url=" + url + "");
		try {
			getMethod.setHeader("User-Agent", USER_AGENT);
			// 添加用户密码验证信息
			// client.getCredentialsProvider().setCredentials(
			// new AuthScope(null, -1),
			// new UsernamePasswordCredentials(mUsername, mPassword));

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 8000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 8000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = httpClient.execute(getMethod);
			// HttpResponse httpResponse = client.execute(getMethod);
			// statusCode == 200 正常
			statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e(TAG, "statuscode = " + statusCode);
			// 处理返回的httpResponse信息
			result = retrieveInputStream(httpResponse.getEntity());

			if (statusCode == 200) {
				tag = true;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage() == null ? null : e.getMessage());
			result = "timeout";

			throw new Exception(e);
		} finally {
			getMethod.abort();
		}

		System.out.println("result===" + result);
		return tag;
	}

	public static String getPostRequest(String url, List<NameValuePair> params,
			boolean isStrEntity) throws Exception {
		String result = null;
		int statusCode = 0;

		HttpPost getMethod = new HttpPost(url);
		if (params != null) {
			String paramstr = addParam(params);
			if (isStrEntity) {
				getMethod.setEntity(new StringEntity(paramstr));
			} else {
				getMethod
						.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
		}

		Log.e(TAG, "do the getRequest,url=" + url + "");
		System.out.println("url=" + url);
		try {
			getMethod.setHeader("User-Agent", USER_AGENT);
			// 添加用户密码验证信息
			// client.getCredentialsProvider().setCredentials(
			// new AuthScope(null, -1),
			// new UsernamePasswordCredentials(mUsername, mPassword));

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = httpClient.execute(getMethod);
			// HttpResponse httpResponse = client.execute(getMethod);
			// statusCode == 200 正常
			statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e(TAG, "statuscode = " + statusCode);
			// 处理返回的httpResponse信息
			result = retrieveInputStream(httpResponse.getEntity());
		} catch (Exception e) {
			//Log.e(TAG, e.getMessage() == null ? null : e.getMessage());
			result = "timeout";
			//System.out.println(e.getMessage());

			//throw new Exception(e);
			return result;
		} finally {
			getMethod.abort();
		}

		System.out.println("result===" + result);
		return result;
	}

	protected static String retrieveInputStream(HttpEntity httpEntity) {
		//Long l = httpEntity.getContentLength();
		int length = (int) httpEntity.getContentLength();
		// the number of bytes of the content, or a negative number if unknown.
		// If the content length is known but exceeds Long.MAX_VALUE, a negative
		// number is returned.
		// length==-1，下面这句报错，println needs a message
		System.out.println("HttpEntity length==" + length);
		if (length < 0)
			length = 10000;
		StringBuffer stringBuffer = new StringBuffer(length);
		String newStr = "";
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					httpEntity.getContent(), HTTP.UTF_8);
			char buffer[] = new char[length];
			int count;
			while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {
				stringBuffer.append(buffer, 0, count);
			}
			newStr = new String(stringBuffer.toString().getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage() == null ? null : e.getMessage());
		} catch (IllegalStateException e) {
			Log.e(TAG, e.getMessage() == null ? null : e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage() == null ? null : e.getMessage());
		}
		return newStr;
	}

	private static String addParam(List<NameValuePair> paramslist) {
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			if (paramslist != null && paramslist.size() > 0) {
				for (int i = 0; i < paramslist.size(); i++) {
					NameValuePair param = paramslist.get(i);
//					/String name = param.getName();
					String value = param.getValue();
					if (i == 0)
						sb.append(value);
					else
						sb.append("&" + value);
				}
				str = sb.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return str;
	}
}
