package com.weixf.menu.auth.springboot.service;

import com.weixf.menu.auth.custom.CommonService;
import com.weixf.menu.auth.entity.SysRole;
import com.weixf.menu.auth.springboot.repository.SysRoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysRoleService extends CommonService<SysRole, Integer> {
    @Resource
    private SysRoleRepository roleRepository;

    public List<SysRole> findByParent(SysRole role) {
        return roleRepository.findByParent(role);
    }

    public SysRole findByName(String name) {
        return roleRepository.findByName(name);
    }

    public List<SysRole> findByParentIsNull() {
        return roleRepository.findByParentIsNull();
    }

    public void deleteByParentId(Integer id) {
        roleRepository.deleteByParentId(id);
    }
}
