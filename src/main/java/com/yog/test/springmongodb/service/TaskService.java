package com.yog.test.springmongodb.service;

import com.yog.test.springmongodb.entity.Task;
import com.yog.test.springmongodb.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    //CRUD

    public Task saveTask(Task task) {
        task.setTaskId(UUID.randomUUID().toString().split("-")[0]);
        return repository.save(task);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task getTask(String taskId) {
        return repository.findById(taskId).get();
    }

    public Task updateTask(Task taskRequest) {
        //get existing object from DB by taskId
        //then set new value from request
        Task existingTask = repository.findById(taskRequest.getTaskId()).get();// DB data
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setPriority(taskRequest.getPriority());
        existingTask.setAssignee(taskRequest.getAssignee());
        existingTask.setStoryPoint(taskRequest.getStoryPoint());
        return repository.save(existingTask);
    }

    public String deleteTask(String taskId) {
        repository.deleteById(taskId);
        return taskId + " task is deleted !!";
    }

    public List<Task> getTaskByAssigneeAndPriority(String assignee, String priority){
        //return repository.findByAssigneeAndPriority(assignee,priority);
        return repository.finTaskWithAssigneeAndPriority(assignee, priority);
    }
}
