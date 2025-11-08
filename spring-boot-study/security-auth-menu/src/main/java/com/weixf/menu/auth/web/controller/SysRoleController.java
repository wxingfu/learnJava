package com.weixf.menu.auth.web.controller;

import com.weixf.menu.auth.custom.AjaxResult;
import com.weixf.menu.auth.custom.CommonController;
import com.weixf.menu.auth.entity.SysRole;
import com.weixf.menu.auth.entity.TbMenu;
import com.weixf.menu.auth.springboot.service.SysRoleService;
import com.weixf.menu.auth.springboot.service.TbMenuService;
import com.weixf.menu.auth.web.form.SysRoleForm;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(value = "/role")
public class SysRoleController extends CommonController<SysRole, Integer, SysRoleForm> {

    @Resource
    private TbMenuService menuService;

    @Resource
    private SysRoleService roleService;

    @RequestMapping(value = "/roleAllocation")
    public String roleAllocation(ModelMap map, Integer id) {
        List<SysRole> roles = roleService.findById(id).getChildren();
        StringBuilder str = new StringBuilder("[");
        for (SysRole r : roles) {
            TbMenu menu = menuService.findByName(r.getName());
            str.append(menu.getId()).append(",");
        }
        str = new StringBuilder(str.substring(0, str.length() - 1));
        if (!"".contentEquals(str)) {
            str.append("]");
        }
        map.put("defaults", str.toString());
        map.put("id", id);
        return "role/roleAllocation";
    }

    @RequestMapping(value = "/treeSave")
    @ResponseBody
    public Object treeSave(String ids, Integer id) {
        try {
            roleService.deleteByParentId(id);
            String[] split = ids.split(",");
            for (String s : split) {
                SysRole role = new SysRole();
                role.setParent(new SysRole(id));
                role.setIdx(menuService.findById(Integer.parseInt(s)).getIdx());
                role.setName(menuService.findById(Integer.parseInt(s)).getName());
                roleService.save(role);
            }
            return new AjaxResult("数据保存成功");
        } catch (Exception e) {
            return new AjaxResult(false, "数据保存失败");
        }
    }

    @RequestMapping(value = "/treeData")
    @ResponseBody
    public Object treeData(Integer id) {
        Sort sort = Sort.by("idx");
        Specification<TbMenu> spec = buildSpecMenu();
        List<TbMenu> list = menuService.findAll(spec, sort);
        return buildTree(list, id);
    }

    private Object buildTree(List<TbMenu> list, Integer id) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (TbMenu dept : list) {
            HashMap<String, Object> node = new HashMap<>();
            node.put("id", dept.getId());
            node.put("name", dept.getName());
            node.put("levelType", 1);
            node.put("parentId", dept.getParentId());
            node.put("children", buildTree(dept.getChildren(), id));
            result.add(node);
        }
        return result;
    }

    private Specification<TbMenu> buildSpecMenu() {
        Specification<TbMenu> specification = new Specification<TbMenu>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<TbMenu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                HashSet<Predicate> rules = new HashSet<>();
                Predicate parent = cb.isNull(root.get("parent"));
                rules.add(parent);
                return cb.and(rules.toArray(new Predicate[rules.size()]));
            }
        };
        return specification;
    }

    @Override
    public Specification<SysRole> buildSpec(SysRoleForm form) {
        Specification<SysRole> specification = new Specification<SysRole>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<SysRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                HashSet<Predicate> rules = new HashSet<>();
                Predicate parent = cb.isNull(root.get("parent"));
                rules.add(parent);
                return cb.and(rules.toArray(new Predicate[rules.size()]));
            }
        };
        return specification;
    }
}
