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

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginTest {

  @Test
  void login() throws MalformedURLException {
    Qbittorrent torrentClient = new Qbittorrent(new URL("http://localhost:8080"));
    Assertions.assertNotNull(torrentClient.login("admin", "adminadmin"));
  }

  @Test
  void getTorrents() throws MalformedURLException {
    Qbittorrent torrentClient = new Qbittorrent(new URL("http://localhost:8080"));
    Assertions.assertNotNull(torrentClient.login("admin", "adminadmin"));
    TorrentsAPI.getTorrents(torrentClient);
  }

  @Test
  void addTorrent() throws MalformedURLException {
    Qbittorrent torrentClient = new Qbittorrent(new URL("http://localhost:8080"));
    Assertions.assertNotNull(torrentClient.login("admin", "adminadmin"));
    TorrentsAPI.addTorrent(
        torrentClient,
        "/home/kamee/Downloads/test",
        "https://ncore.cc/torrents.php?action=download&id=2406790&key=a730fce30811233cd2cd3cc04a5491e2",
        "https://ncore.cc/torrents.php?action=download&id=2406855&key=a730fce30811233cd2cd3cc04a5491e2");
  }

  @Test
  void addFileTorrent() throws MalformedURLException, URISyntaxException {
    Qbittorrent torrentClient = new Qbittorrent(new URL("http://localhost:8080"));
    Assertions.assertNotNull(torrentClient.login("admin", "adminadmin"));
    URL resource = getClass().getResource("torrent.torrent");
    File torrent = new File(resource.toURI());
    resource = getClass().getResource("asd.torrent");
    File torrent2 = new File(resource.toURI());

    TorrentsAPI.addTorrent(torrentClient, "/home/kamee/Downloads/test", torrent, torrent2);
  }

  @Test
  void addByteTorrent() throws IOException, URISyntaxException {
    Qbittorrent torrentClient = new Qbittorrent(new URL("http://localhost:8080"));
    Assertions.assertNotNull(torrentClient.login("admin", "adminadmin"));
    URL resource = getClass().getResource("torrent.torrent");
    File torrent = new File(resource.toURI());
    resource = getClass().getResource("asd.torrent");
    File torrent2 = new File(resource.toURI());
    Map<String, byte[]> files = new HashMap<>();
    files.put(torrent.getName(), FileUtils.readFileToByteArray(torrent));
    files.put(torrent2.getName(), FileUtils.readFileToByteArray(torrent2));
    TorrentsAPI.addTorrent(torrentClient, "/home/kamee/Downloads/test", files);
  }
}
