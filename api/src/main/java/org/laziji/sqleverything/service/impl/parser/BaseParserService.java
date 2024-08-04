package org.laziji.sqleverything.service.impl.parser;

import org.laziji.sqleverything.consts.FileType;
import org.laziji.sqleverything.service.ParserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseParserService implements ParserService {

    private static final Map<FileType, ParserService> cache = new ConcurrentHashMap<>();

    {
        cache.put(getFileType(), this);
    }

    protected abstract FileType getFileType();

    public static ParserService getInstance(FileType fileType) {
        return cache.get(fileType);
    }



}
