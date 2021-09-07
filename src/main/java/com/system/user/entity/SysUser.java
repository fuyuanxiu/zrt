package com.system.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户基础信息表
 *
 */
@Entity(name = "SysUser")
@Table(name = SysUser.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysUser extends com.app.base.entity.BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "app_sys_user";
    
    /**
     * 用户账号
     */
    @ApiModelProperty(name = "bsCode", value = "用户账号")
    @Column(length = 50)
    protected String bsCode;

    /**
     * 用户名称
     */
    @ApiModelProperty(name = "bsName", value = "用户名称")
    @Column(length = 100)
    protected String bsName;
   
    /**
     * 用户类型（0：zrt用户/1：客户用户）
     */
    @ApiModelProperty(name = "bsType", value = "用户类型（0：zrt用户/1：客户用户）")
    @Column
    protected Integer bsType = 1;
    
    /**
     * 密码
     */
    @ApiModelProperty(name = "bsPassword", value = "密码")
    @Column(length = 50)
    protected String bsPassword;

    /**
     * 状态（0：正常 / 1：禁用）
     */
    @ApiModelProperty(name = "bsStatus", value = "状态（0：正常 / 1：禁用）")
    @Column
    protected Integer bsStatus = 0;
    
    /**
     * 邮箱
     */
    @ApiModelProperty(name = "bsEmail", value = "邮箱")
    @Column(length = 100)
    protected String bsEmail;
    
    /**
     * 所在地
     */
    @ApiModelProperty(name = "bsLocate", value = "所在地")
    @Column(length = 100)
    protected String bsLocate;
        
    /**
     * 联系人名
     */
    @ApiModelProperty(name = "bsUser", value = "联系人名")
    @Column(length = 50)
    protected String bsUser;
    
    /**
     * 联系人电话
     */
    @ApiModelProperty(name = "bsMobile", value = "联系人电话")
    @Column(length = 50)
    protected String bsMobile;

    /**
     * 送货地址
     */
    @ApiModelProperty(name = "bsAddress", value = "送货地址")
    @Column(length = 225)
    protected String bsAddress;
    
    /**
     * 备注
     */
    @ApiModelProperty(name = "bsMemo", value = "备注")
    @Column(length = 225)
    protected String bsMemo;
    

    /**
     * 角色ID
     */
    @Transient
    protected String roleIds;


	public String getBsCode() {
		return bsCode;
	}


	public void setBsCode(String bsCode) {
		this.bsCode = bsCode;
	}


	public String getBsName() {
		return bsName;
	}


	public void setBsName(String bsName) {
		this.bsName = bsName;
	}


	public Integer getBsType() {
		return bsType;
	}


	public void setBsType(Integer bsType) {
		this.bsType = bsType;
	}


	public String getBsPassword() {
		return bsPassword;
	}


	public void setBsPassword(String bsPassword) {
		this.bsPassword = bsPassword;
	}


	public Integer getBsStatus() {
		return bsStatus;
	}


	public void setBsStatus(Integer bsStatus) {
		this.bsStatus = bsStatus;
	}


	public String getBsEmail() {
		return bsEmail;
	}


	public void setBsEmail(String bsEmail) {
		this.bsEmail = bsEmail;
	}


	public String getBsLocate() {
		return bsLocate;
	}


	public void setBsLocate(String bsLocate) {
		this.bsLocate = bsLocate;
	}


	public String getBsUser() {
		return bsUser;
	}


	public void setBsUser(String bsUser) {
		this.bsUser = bsUser;
	}


	public String getBsMobile() {
		return bsMobile;
	}


	public void setBsMobile(String bsMobile) {
		this.bsMobile = bsMobile;
	}


	public String getBsAddress() {
		return bsAddress;
	}


	public void setBsAddress(String bsAddress) {
		this.bsAddress = bsAddress;
	}


	public String getBsMemo() {
		return bsMemo;
	}


	public void setBsMemo(String bsMemo) {
		this.bsMemo = bsMemo;
	}


	public String getRoleIds() {
		return roleIds;
	}


	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
    
}
