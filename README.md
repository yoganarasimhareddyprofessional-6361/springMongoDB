# 3. SPRING DATA MONGO DB

| Types | NOSQL | SQL (RDBMS) |
| --- | --- | --- |
| Services | MongoDB, Cassandra, Neo4j, CouchBase, Dynamo DB etc.. | Oracle, Mysql, Postgress etc.. |
| Format  | JSON format  | Structured Queries |
| In Spring (POJO) | Document | Entity/DAO |

## Example Project —> SpringMongo

- Step 1:
    - Create a database on mongo DB and copy the connect string from mongo atlas
    - spring.data.mongodb.uri= mongodb+srv://yoganarasimhareddyprofessional_db_user:<password>@springmongodb.eqo3ycm.mongodb.net/<databasename>?retryWrites=true&w=majority&appName=springMongoDB
    - https://www.mongodb.com/cloud/atlas
- Step2
    - Create Spring boot propject >> springMongoDB
    - add details and dependencies >> spring web >> spring mongo DB >> lombok>>dev tools >> create
    - copy the connect string on application.properties
    - application.properties

    ```jsx
    spring.application.name=springMongoDB
    
    server.port=9996
    spring.data.mongodb.uri= mongodb+srv://yoganarasimhareddyprofessional_db_user:<password>@springmongodb.eqo3ycm.mongodb.net/<databasename>?retryWrites=true&w=majority&appName=springMongoDB
    ```

- Step3
    - create a entity class in mongo DB/ NoSql its called Document >>
    -

    ```java
    package com.yog.test.springmongodb.entity;
    
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.mapping.Document;
    
    //collection same as table in RDBMS
    @Document(collection = "Tasks")   //@Entity   @Table(name="")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Task {
        @Id
        private String taskId;
        private String description;
        private String priority; //p1 ,p2 , p3
        private String assignee;
        private int storyPoint; //5
    
    }
    ```

- Step 4:
    - Create a Repository
    -

    ```java
    package com.yog.test.springmongodb.repository;
    
    import com.yog.test.springmongodb.entity.Task;
    import org.springframework.data.mongodb.repository.MongoRepository;
    
    public interface TasksRepository extends MongoRepository<Task,String> {
    }
    
    ```

- Step 5:
    - create a service layer
    -

    ```java
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
    
    ```

- Step 6: custom Repository Queries
-

```java
package com.yog.test.springmongodb.repository;

import com.yog.test.springmongodb.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByAssigneeAndPriority(String assignee, String priority);

    @Query(value = "{assignee: ?0 ,priority: ?1}", fields = "{'description' : 1 , 'storyPoint': 2}")
    List<Task> finTaskWithAssigneeAndPriority(String assignee, String priority);

    //operator (IN/LIKE/BETWEEN)
    //pagination & sorting
}

```

- Step 7: create Controller
-

```java
package com.yog.test.springmongodb.controller;

import com.yog.test.springmongodb.entity.Task;
import com.yog.test.springmongodb.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task addNewTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @GetMapping
    public List<Task> findAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{taskId}")
    public Task findTaskByTaskId(@PathVariable String taskId) {
        return taskService.getTask(taskId);
    }

    @PutMapping
    public Task updateTask(@RequestBody Task taskRequest) {
        return taskService.updateTask(taskRequest);
    }

    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable String taskId) {
        return taskService.deleteTask(taskId);
    }

    @GetMapping("/assignee/{assignee}/priority/{priority}")
    public List<Task> getTaskByAssigneeAndPriority(@PathVariable String assignee, @PathVariable String priority) {
        return taskService.getTaskByAssigneeAndPriority(assignee, priority);
    }

}
```

```java
Input request body :

{
    "description" : "create UI",
    "priority" : "p1",
    "assignee" : "yog",
    "storyPoint" : "2"
    
}
```

Note : we can also use simillar actions as OPERATORS , Pagination, Sorting and all custom/JPA query structure followed on RDBMS/JPARepository.