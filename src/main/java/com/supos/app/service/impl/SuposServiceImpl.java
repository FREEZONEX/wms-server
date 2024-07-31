package com.supos.app.service.impl;

import com.bluetron.eco.sdk.api.SuposApiEnum;
import com.bluetron.eco.sdk.api.SuposApiV2;
import com.bluetron.eco.sdk.dto.common.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.supos.app.domain.entity.SuposUser;
import com.supos.app.mapper.SuposUserMapper;
import com.supos.app.service.SuposService;
import com.supos.app.service.SuposUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SuposServiceImpl implements SuposService {

    @Autowired
    SuposUserService suposUserService;

    @Resource(name = "sqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void syncUserData() {
        Map<String, Object> param = ImmutableMap.of("pageIndex", 1, "pageSize", 499, "companyCode", "default_org_company");
         ApiResponse supResponse = SuposApiV2.get(SuposApiEnum.USER_LISTS.getUrl(), param);
        if (supResponse.getHttpCode() != 200) {
            log.error("Failed to access the supos platform.");
            return;
        }
        String userStr = supResponse.getResponseBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(userStr);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        if (jsonNode == null) {
            log.error("Failed to synchronize user data from the supos platform");
            return;
        }
        JsonNode nodes = jsonNode.get("list");
        if (nodes != null && nodes.isArray()) {
            SuposUser user;

            List<SuposUser> userWmsList = suposUserService.selectAll();

            List<SuposUser> addUserList = new ArrayList<>();
            List<SuposUser> updateUserList = new ArrayList<>();
            for (JsonNode userNode : nodes) {
                user = new SuposUser();

                String personCode = userNode.get("personCode").asText();
                String modifyTime = userNode.get("modifyTime").asText();
                Date modifyDate = this.convertDate(modifyTime);

                List<SuposUser> userWMSExist = userWmsList.stream()
                        .filter(obj -> obj.getPersoncode().equals(personCode))
                        .collect(Collectors.toList());

                if (userWMSExist.isEmpty()) {//new
                    SuposUser savingUser = this.getSuposUser(user, userNode);
                    addUserList.add(savingUser);
                } else {//modify
                    for (SuposUser userWms : userWMSExist) {
                        if (userWms.getModifytime().equals(modifyDate)) //The user already exists and has been modified from supos side
                            continue;
                        user.setId(userWms.getId());
                        SuposUser savingUser = this.getSuposUser(user, userNode);
                        //keep accounttype value for wms modification
                        savingUser.setAccounttype(userWms.getAccounttype());
                        updateUserList.add(savingUser);
                    }
                }
            }

            if (!addUserList.isEmpty())
                suposUserService.insertBatch(addUserList);
            if (!updateUserList.isEmpty())
                this.updateUserList(updateUserList);
        }
    }


    private void updateUserList(List<SuposUser> userList) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        SuposUserMapper mapper = sqlSession.getMapper(SuposUserMapper.class);
        try {
            for (SuposUser entity : userList) {
                mapper.update(entity);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    private SuposUser getSuposUser(SuposUser user, JsonNode userNode) {
        user.setUsername(userNode.get("username").asText());
        user.setAccounttype(userNode.get("accountType").asInt());
        user.setLockstatus(userNode.get("lockStatus").asInt());
        user.setValid(userNode.get("valid").asInt());
        user.setPersoncode(userNode.get("personCode").asText());
        user.setPersonname(userNode.get("personName").asText());
        String modifyTime = userNode.get("modifyTime").asText();
        Date currentDate = new Date(System.currentTimeMillis());
        String createTime = userNode.get("createTime").asText();
        user.setCreatetime(createTime != null ? this.convertDate(createTime) : currentDate);
        user.setModifytime(modifyTime != null ? this.convertDate(modifyTime) : currentDate);
        user.setSynctime(currentDate);
        return user;
    }

    private Date convertDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        if (dateStr != null) {
            try {
                date = sdf.parse(dateStr);
            } catch (Exception exception) {
                log.error(exception.getMessage());
            }
        }
        return date;
    }
}
