package com.jagregory.shiro.tags;

import com.jagregory.shiro.freemarker.PermissionTag;
import org.apache.shiro.subject.Subject;


public class HasAnyPermissionsTag extends PermissionTag {
    // Delimeter that separates role names in tag attribute
    private static final String ROLE_NAMES_DELIMETER = ",";

    @Override
    protected boolean showTagBody(String permissions) {
        boolean hasAnyPermissions = false;
        Subject subject = getSubject();
        if (subject != null) {
            for (String permission : permissions.split(ROLE_NAMES_DELIMETER)) {
                if (subject.isPermitted(permission.trim())) {
                    hasAnyPermissions = true;
                    break;
                }
            }
        }
        return hasAnyPermissions;
    }
}
