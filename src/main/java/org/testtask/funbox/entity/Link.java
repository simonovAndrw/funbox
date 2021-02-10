package org.testtask.funbox.entity;

import com.google.gson.annotations.Expose;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link {

    @Expose
    private Long id = System.currentTimeMillis();
    private String[] links;
}
