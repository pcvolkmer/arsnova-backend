/*
 * This file is part of ARSnova Backend.
 * Copyright (C) 2012-2018 The ARSnova Team and Contributors
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
package de.thm.arsnova.cache;

import de.thm.arsnova.event.AfterCreationEvent;
import de.thm.arsnova.event.AfterDeletionEvent;
import de.thm.arsnova.event.ChangeScoreEvent;
import de.thm.arsnova.model.Answer;
import de.thm.arsnova.model.Comment;
import de.thm.arsnova.model.Room;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This class is used to evict caches based on events. The events carry all necessary information to clear the
 * caches, e.g, for a specific session.
 */
@Component
public class CacheBusterImpl implements CacheBuster {

	@CacheEvict(value = "statistics", allEntries = true)
	@EventListener
	public void handleAfterCommentCreation(AfterCreationEvent<Comment> event) { }

	@CacheEvict(value = "statistics", allEntries = true)
	@EventListener
	public void handleAfterCommentDeletion(AfterDeletionEvent<Comment> event) { }

	@CacheEvict(value = "answerlists", key = "#event.content.id")
	@EventListener
	public void handleAfterAnswerCreation(AfterCreationEvent<Answer> event) { }

	@CacheEvict(value = "statistics", allEntries = true)
	@EventListener
	public void handleChangeScore(ChangeScoreEvent event) { }

	@CacheEvict(value = "statistics", allEntries = true)
	@EventListener
	public void handleAfterRoomCreation(AfterCreationEvent<Room> event) { }

	@CacheEvict(value = "statistics", allEntries = true)
	@EventListener
	public void handleAfterRoomDeletion(AfterDeletionEvent<Room> event) { }
}
