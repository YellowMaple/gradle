/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.internal.scripts;

import org.gradle.internal.service.DefaultServiceLocator;
import org.gradle.scripts.ScriptingLanguage;

import java.util.Iterator;

public class DefaultScriptingLanguages implements ScriptingLanguages {

    public static ScriptingLanguages create() {
        return new DefaultScriptingLanguages(new DefaultServiceLocator(DefaultScriptingLanguages.class.getClassLoader()).getAll(ScriptingLanguage.class));
    }

    static ScriptingLanguages createLenient() {
        return new DefaultScriptingLanguages(new DefaultServiceLocator(DefaultScriptingLanguages.class.getClassLoader()).getAllLenient(ScriptingLanguage.class));
    }

    private final Iterable<ScriptingLanguage> scriptingLanguages;

    private DefaultScriptingLanguages(Iterable<ScriptingLanguage> scriptingLanguages) {
        this.scriptingLanguages = scriptingLanguages;
    }

    @Override
    public Iterator<ScriptingLanguage> iterator() {
        return scriptingLanguages.iterator();
    }
}
