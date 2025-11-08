package com.weixf.schema.maker.pdm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 *
 * @since 2022-01-21
 */
@Setter
@Getter
@Component
public class PDMUser {

    public String Id;
    private String Name;
    private String Code;

    public PDMUser() {
    }

}
