/*
 * Some open source application
 *
 * Copyright 2018 by it's authors.
 *
 * Licensed under GNU General Public License 3.0 or later.
 * Some rights reserved. See LICENSE, AUTHORS.
 *
 * @license GPL-3.0+ <https://opensource.org/licenses/GPL-3.0>
 */
package com.kameecoding.qbittorrent_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TorrentsAPI {
  private static final Logger LOGGER = LogManager.getLogger(TorrentsAPI.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private TorrentsAPI() {}

  public static ArrayNode getTorrents(Qbittorrent qbittorrent) {
    ArrayNode result = null;

    try {
      URL url = qbittorrent.getURL(API_METHODS.TORRENT_LIST);
      CloseableHttpClient client = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(url.toURI());
      httpPost.addHeader("Cookie", "SID=" + qbittorrent.getCookie());
      CloseableHttpResponse httpResponse = client.execute(httpPost);
      JsonNode jsonNode = OBJECT_MAPPER.readTree(httpResponse.getEntity().getContent());
      if (jsonNode.isArray()) {
        result = (ArrayNode) jsonNode;
      }
    } catch (MalformedURLException e) {
      LOGGER.error(e);
    } catch (URISyntaxException e) {
      LOGGER.error(e);
    } catch (ClientProtocolException e) {
      LOGGER.error(e);
    } catch (IOException e) {
      LOGGER.error(e);
    }

    return result;
  }

  public static boolean addTorrent(Qbittorrent qbittorrent, String savePath, String... urls) {
    try {
      URL url = qbittorrent.getURL(API_METHODS.ADD_TORRENT);
      CloseableHttpClient client = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(url.toURI());
      httpPost.addHeader("Cookie", "SID=" + qbittorrent.getCookie());
      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      StringBuilder torrentUrls = new StringBuilder();
      for (String torrentUrl : urls) {
        torrentUrls.append(torrentUrl).append("\n");
      }
      multipartEntityBuilder.addTextBody("urls", torrentUrls.toString());
      multipartEntityBuilder.addTextBody("savepath", savePath);
      HttpEntity httpEntity = multipartEntityBuilder.build();

      httpPost.setEntity(httpEntity);
      CloseableHttpResponse httpResponse = client.execute(httpPost);

      return httpResponse.getStatusLine().getStatusCode() == 200;
    } catch (MalformedURLException e) {
      LOGGER.error(e);
    } catch (Exception e) {
      LOGGER.error(e);
    }

    return false;
  }

  public static boolean addTorrent(Qbittorrent qbittorrent, String savePath, File... files) {
    try {
      URL url = qbittorrent.getURL(API_METHODS.ADD_TORRENT);
      CloseableHttpClient client = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(url.toURI());
      httpPost.addHeader("Cookie", "SID=" + qbittorrent.getCookie());
      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      multipartEntityBuilder.addTextBody("savepath", savePath);
      for (File file : files) {
        multipartEntityBuilder.addBinaryBody("torrents", file);
      }
      httpPost.setEntity(multipartEntityBuilder.build());
      CloseableHttpResponse httpResponse = client.execute(httpPost);

      return httpResponse.getStatusLine().getStatusCode() == 200;
    } catch (MalformedURLException e) {
      LOGGER.error(e);
    } catch (Exception e) {
      LOGGER.error(e);
    }

    return false;
  }

  public static boolean addTorrent(Qbittorrent qbittorrent, String savePath, Map<String, byte[]> files) {
    try {
      URL url = qbittorrent.getURL(API_METHODS.ADD_TORRENT);
      CloseableHttpClient client = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(url.toURI());
      httpPost.addHeader("Cookie", "SID=" + qbittorrent.getCookie());
      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      multipartEntityBuilder.addTextBody("savepath", savePath);
      for (Map.Entry<String, byte[]> file : files.entrySet()) {
        multipartEntityBuilder.addBinaryBody("torrents", file.getValue(), ContentType.DEFAULT_BINARY, file.getKey());
      }
      httpPost.setEntity(multipartEntityBuilder.build());
      CloseableHttpResponse httpResponse = client.execute(httpPost);

      return httpResponse.getStatusLine().getStatusCode() == 200;
    } catch (MalformedURLException e) {
      LOGGER.error(e);
    } catch (Exception e) {
      LOGGER.error(e);
    }

    return false;
  }
}
