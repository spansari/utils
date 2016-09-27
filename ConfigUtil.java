package com.macys.mst.wms.atlas.common.utils;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ConfigUtil implements Runnable {
    config;

    private static final String ATLAS_CONFIG = "Atlas-Config.properties";

    private static final String PROPERTIES_DIR_KEY = "propsLocation";

    private Properties configFile;

    public String get(String key) {
	if (this.configFile == null) {
	    init();
	}
	return this.configFile.getProperty(key);
    }

    private void init() {
	load(ATLAS_CONFIG, this.configFile);
	//new Thread(ConfigUtil.config).start();
    }

    private void load(String propFileName, Properties configFile) {
	this.configFile = new java.util.Properties();
	try {
	    File initialFile = new File(System.getProperty(PROPERTIES_DIR_KEY) + File.separator + propFileName);
	    InputStream inStream = new FileInputStream(initialFile);

	    this.configFile.load(inStream);

	} catch (Exception e) {
	    try {
		configFile.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName));
	    } catch (IOException e1) {
	    	 log.error("Error reading Atlas-Config.properties:{}", e1);
	    }
	    log.error("Error reading Atlas-Config.properties:{}", e);
	}

    }

    private void startWatcher() {
	try {
	    WatchService watcher = FileSystems.getDefault().newWatchService();
	    Path dir = Paths.get(System.getProperty(PROPERTIES_DIR_KEY));
	    dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

	    log.info("File Watch Service registered for dir: " + dir.getFileName());

	    while (true) {
		WatchKey key;
		try {
		    key = watcher.take();
		} catch (InterruptedException ex) {
		    return;
		}

		for (WatchEvent<?> event : key.pollEvents()) {
		    WatchEvent.Kind<?> kind = event.kind();
		    if (kind == OVERFLOW) {
			continue;
		    }

		    @SuppressWarnings("unchecked")
		    WatchEvent<Path> ev = (WatchEvent<Path>) event;
		    Path fileName = ev.context();

		    log.debug(kind.name() + ": " + fileName);

		    if (kind == ENTRY_MODIFY || kind == ENTRY_DELETE  && fileName.toString().equals(ATLAS_CONFIG)) {
			log.info("Properties file has changed!!!");
			load(ATLAS_CONFIG, this.configFile);
		    }
		}

		boolean valid = key.reset();
		if (!valid) {
		    break;
		}
	    }

	} catch (IOException ex) {
	    log.error(ex.getMessage(), ex);
	}
    }

    @Override
    public void run() {
	startWatcher();
    }

    public static void main(String[] args) {
	System.out.println(ConfigUtil.config.get("items.per.page"));
	// System.out.println(ConfigUtil.config.get("items.per.page"));
    }

}
