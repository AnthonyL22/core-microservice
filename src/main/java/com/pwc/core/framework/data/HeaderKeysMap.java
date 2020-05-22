package com.pwc.core.framework.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class HeaderKeysMap {

    private HashMap<String, String> authorizationMap;

}
