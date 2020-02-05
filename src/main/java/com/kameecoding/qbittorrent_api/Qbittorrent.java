package com.kameecoding.qbittorrent_api;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** */
public class Qbittorrent {

  private final URL WEB_UI_IP;
  private static final Pattern COOKIE_PATTERN = Pattern.compile("SID=(.*?);.*");

  private String cookie;

  public Qbittorrent(URL webUiAdress) {
    WEB_UI_IP = webUiAdress;
  }

  public String login(String username, String password) {
    try {
      URL loginUrl = getURL(API_METHODS.LOGIN);
      CloseableHttpClient client = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(loginUrl.toURI());

      List<NameValuePair> params = new ArrayList<>();
      params.add(new BasicNameValuePair("username", username));
      params.add(new BasicNameValuePair("password", password));
      httpPost.setEntity(new UrlEncodedFormEntity(params));
      CloseableHttpResponse response = client.execute(httpPost);
      if (response.containsHeader("set-cookie")) {
        Matcher matcher = COOKIE_PATTERN.matcher(response.getHeaders("set-cookie")[0].getValue());
        if (matcher.matches()) {
          cookie = matcher.group(1);
          return matcher.group(1);
        }
      }
    } catch (MalformedURLException e) {
      // TODO log error;
      return null;
    } catch (IOException e) {
      // TODO log error;
      return null;
    } catch (URISyntaxException e) {
      // TODO log error;
      return null;
    }
    return null;
  }

  public URL getURL(API_METHODS method) throws MalformedURLException {
    StringBuilder urlBuilder = new StringBuilder(WEB_UI_IP.getProtocol()).append("://");
    urlBuilder.append(WEB_UI_IP.getHost()).append(":").append(WEB_UI_IP.getPort());
    urlBuilder.append(method.getPath());
    return new URL(urlBuilder.toString());
  }

  public void logout() throws IOException, URISyntaxException {
    cookie = null;
    URL logoutURL = getURL(API_METHODS.LOGOUT);
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost get = new HttpPost(logoutURL.toURI());
    client.execute(get);
  }

  public String getCookie() {
    return cookie;
  }
}
