package com.pwc.core.framework.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class HeaderKeysMap {

    private HashMap<String, String> authorizationMap;

}
