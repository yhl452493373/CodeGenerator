//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jagregory.shiro.freemarker;

import com.jagregory.shiro.tags.HasAnyPermissionsTag;
import freemarker.template.SimpleHash;

@SuppressWarnings("deprecation")
public class ShiroTags extends SimpleHash {
    public ShiroTags() {
        this.put("authenticated", new AuthenticatedTag());
        this.put("guest", new GuestTag());
        this.put("hasAnyRoles", new HasAnyRolesTag());
        this.put("hasPermission", new HasPermissionTag());
        this.put("hasAnyPermissions", new HasAnyPermissionsTag());
        this.put("hasRole", new HasRoleTag());
        this.put("lacksPermission", new LacksPermissionTag());
        this.put("lacksRole", new LacksRoleTag());
        this.put("notAuthenticated", new NotAuthenticatedTag());
        this.put("principal", new PrincipalTag());
        this.put("user", new UserTag());
    }
}
