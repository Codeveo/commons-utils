/*
 * Copyright 2020 Codeveo Ltd.
 *
 * Written by Ladislav Klenovic <lklenovic@codeveo.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.codeveo.commons.utils;

import com.codeveo.commons.utils.exception.CodeveoUtilsException;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class IoUtils {

    private IoUtils(){
    }

    /**
     * Get file input stream from path. If the path starts with 'classpath:' then the classpath is used.
     *
     * @param path file path
     * @return file input stream
     * @throws CodeveoUtilsException in case of error
     */
    public static InputStream getInputStream(final String path) {
        try {
            return path.startsWith("classpath:") ? Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(path.substring("classpath:".length()))
                    : new FileInputStream(path);
        } catch (final FileNotFoundException e) {
            throw new CodeveoUtilsException("Error occurred while creating input stream to " + path, e);
        }
    }

    /**
     * Read file from path. If the path starts with 'classpath:' then the classpath is used.
     *
     * @param path file path
     * @return file content
     * @throws CodeveoUtilsException in case of error
     */
    public static String readFile(final String path) {
        try (final InputStream in = getInputStream(path)) {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new CodeveoUtilsException("Error occurred while reading file from " + path, e);
        }
    }
}
