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

public enum API_METHODS {
  LOGIN("/api/v2/auth/login"),
  LOGOUT("/api/v2/app/logout"),
  ADD_TORRENT("/api/v2/torrents/add"),
  TORRENT_LIST("/api/v2/torrents/info");

  private final String path;

  API_METHODS(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
