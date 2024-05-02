package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 选题审核
 *
 * @author 
 * @email
 * @date 2021-04-23
 */
@TableName("xuantishenhe")
public class XuantishenheEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public XuantishenheEntity() {

	}

	public XuantishenheEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")

    private Integer id;


    /**
     * 选择学生
     */
    @TableField(value = "yonghu_id")

    private Integer yonghuId;


    /**
     * 选择题目
     */
    @TableField(value = "jiaoshi_id")

    private Integer jiaoshiId;


    /**
     * 发布教师
     */
    @TableField(value = "shejitimu_id")

    private Integer shejitimuId;


    /**
     * 审核状态
     */
    @TableField(value = "xuantishenhe_types")

    private Integer xuantishenheTypes;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
	 * 设置：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 获取：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 设置：选择学生
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }


    /**
	 * 获取：选择学生
	 */

    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 设置：选择题目
	 */
    public Integer getJiaoshiId() {
        return jiaoshiId;
    }


    /**
	 * 获取：选择题目
	 */

    public void setJiaoshiId(Integer jiaoshiId) {
        this.jiaoshiId = jiaoshiId;
    }
    /**
	 * 设置：发布教师
	 */
    public Integer getShejitimuId() {
        return shejitimuId;
    }


    /**
	 * 获取：发布教师
	 */

    public void setShejitimuId(Integer shejitimuId) {
        this.shejitimuId = shejitimuId;
    }
    /**
	 * 设置：审核状态
	 */
    public Integer getXuantishenheTypes() {
        return xuantishenheTypes;
    }


    /**
	 * 获取：审核状态
	 */

    public void setXuantishenheTypes(Integer xuantishenheTypes) {
        this.xuantishenheTypes = xuantishenheTypes;
    }
    /**
	 * 设置：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 获取：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Xuantishenhe{" +
            "id=" + id +
            ", yonghuId=" + yonghuId +
            ", jiaoshiId=" + jiaoshiId +
            ", shejitimuId=" + shejitimuId +
            ", xuantishenheTypes=" + xuantishenheTypes +
            ", createTime=" + createTime +
        "}";
    }
}
