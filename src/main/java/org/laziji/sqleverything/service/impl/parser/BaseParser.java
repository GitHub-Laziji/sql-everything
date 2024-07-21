package org.laziji.sqleverything.service.impl.parser;

import org.laziji.sqleverything.consts.FileType;
import org.laziji.sqleverything.service.Parser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseParser implements Parser {

    private static final Map<FileType, Parser> cache = new ConcurrentHashMap<>();

    {
        cache.put(getFileType(), this);
    }

    protected abstract FileType getFileType();

    public static Parser getInstance(FileType fileType) {
        return cache.get(fileType);
    }

}
