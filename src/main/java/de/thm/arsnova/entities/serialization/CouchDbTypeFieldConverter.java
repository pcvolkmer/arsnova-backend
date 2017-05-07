/*
 * This file is part of ARSnova Backend.
 * Copyright (C) 2012-2017 The ARSnova Team
 *
 * ARSnova Backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ARSnova Backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.thm.arsnova.entities.serialization;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import de.thm.arsnova.entities.Entity;
import de.thm.arsnova.entities.LogEntry;
import java.util.HashMap;
import java.util.Map;

public class CouchDbTypeFieldConverter implements Converter<Class<? extends Entity>, String> {
	private static final Map<Class<? extends Entity>, String> typeMapping = new HashMap<>();

	{
		typeMapping.put(LogEntry.class, "log");
	}

	@Override
	public String convert(Class<? extends Entity> aClass) {
		return typeMapping.get(aClass);
	}

	@Override
	public JavaType getInputType(TypeFactory typeFactory) {
		return typeFactory.constructGeneralizedType(typeFactory.constructType(Class.class), Entity.class);
	}

	@Override
	public JavaType getOutputType(TypeFactory typeFactory) {
		return typeFactory.constructType(String.class);
	}
}
