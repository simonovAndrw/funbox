package org.testtask.funbox.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    private String status;

    public static Status getOkStatus() {
        return new Status("Ok");
    }

    public static Status getErrorStatus() {
        return new Status("Error");
    }
}


