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