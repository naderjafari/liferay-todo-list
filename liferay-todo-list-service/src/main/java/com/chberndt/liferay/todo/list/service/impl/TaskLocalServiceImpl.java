/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.chberndt.liferay.todo.list.service.impl;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.base.TaskLocalServiceBaseImpl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.ModelPermissions;

import java.util.Date;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the task local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * <code>com.chberndt.liferay.todo.list.service.TaskLocalService</code>
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @see TaskLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = AopService.class
)
public class TaskLocalServiceImpl extends TaskLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use
	 * <code>com.chberndt.liferay.todo.list.service.TaskLocalService</code> via
	 * injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use
	 * <code>com.chberndt.liferay.todo.list.service.TaskLocalServiceUtil</code>.
	 */
	@Override
	public Task addTask(
			long userId, String title, String description, boolean completed,
			Date dueDate, ServiceContext serviceContext)
		throws PortalException {

		// Task

		User user = userLocalService.getUser(userId);
		long groupId = serviceContext.getScopeGroupId();

		// TODO: validate

		long taskId = counterLocalService.increment();

		// TODO: friendlyURL validation

		Task task = taskPersistence.create(taskId);

		task.setUuid(serviceContext.getUuid());
		task.setGroupId(groupId);
		task.setCompanyId(user.getCompanyId());
		task.setUserId(user.getUserId());
		task.setUserName(user.getFullName());
		task.setTitle(title);
		task.setDescription(description);
		task.setCompleted(completed);
		task.setDueDate(dueDate);

		task = taskPersistence.update(task);

		// Resources

		if (serviceContext.isAddGroupPermissions() ||
			serviceContext.isAddGuestPermissions()) {

			addTaskResources(
				task, serviceContext.isAddGroupPermissions(),
				serviceContext.isAddGuestPermissions());
		}
		else {
			addTaskResources(task, serviceContext.getModelPermissions());
		}

		// TODO: Asset

		// TODO: Workflow

		return task;
	}

	@Override
	public void addTaskResources(
			long taskId, boolean addGroupPermissions,
			boolean addGuestPermissions)
		throws PortalException {

		Task task = taskPersistence.findByPrimaryKey(taskId);

		addTaskResources(task, addGroupPermissions, addGuestPermissions);
	}

	@Override
	public void addTaskResources(long taskId, ModelPermissions modelPermissions)
		throws PortalException {

		Task task = taskPersistence.findByPrimaryKey(taskId);

		addTaskResources(task, modelPermissions);
	}

	@Override
	public void addTaskResources(
			Task task, boolean addGroupPermissions, boolean addGuestPermissions)
		throws PortalException {

		resourceLocalService.addResources(
			task.getCompanyId(), task.getGroupId(), task.getUserId(),
			Task.class.getName(), task.getTaskId(), false, addGroupPermissions,
			addGuestPermissions);
	}

	@Override
	public void addTaskResources(Task task, ModelPermissions modelPermissions)
		throws PortalException {

		resourceLocalService.addModelResources(
			task.getCompanyId(), task.getGroupId(), task.getUserId(),
			Task.class.getName(), task.getTaskId(), modelPermissions);
	}

	@Override
	public Task deleteTask(Task task) throws PortalException {

		// Task

		taskPersistence.remove(task);

		// Resources

		resourceLocalService.deleteResource(
			task.getCompanyId(), Task.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL, task.getTaskId());

		// TODO: Subscriptions

		// TODO: Asset

		// TODO: Expando

		// TODO: Trash

		// TODO: Workflow

		return task;
	}

	@Override
	public Task getTask(long taskId) throws PortalException {
		return taskPersistence.findByPrimaryKey(taskId);
	}

	public Task getTask(long groupId, String title) throws PortalException {
		return taskPersistence.fetchByG_T_First(groupId, title, null);
	}

	// TODO: updateTask

}