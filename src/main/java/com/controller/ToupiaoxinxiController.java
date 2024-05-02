package com.controller;


import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.StringUtil;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import com.entity.ToupiaoxinxiEntity;

import com.service.ToupiaoxinxiService;
import com.entity.view.ToupiaoxinxiView;
import com.service.JiaoshiService;
import com.entity.JiaoshiEntity;
import com.service.ShejitimuService;
import com.entity.ShejitimuEntity;

import com.utils.PageUtils;
import com.utils.R;

/**
 * 投票记录
 * 后端接口
 * @author
 * @email
 * @date 2021-04-23
*/
@RestController
@Controller
@RequestMapping("/toupiaoxinxi")
public class ToupiaoxinxiController {
    private static final Logger logger = LoggerFactory.getLogger(ToupiaoxinxiController.class);

    @Autowired
    private ToupiaoxinxiService toupiaoxinxiService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;



    //级联表service
    @Autowired
    private JiaoshiService jiaoshiService;
    @Autowired
    private ShejitimuService shejitimuService;


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
     
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isNotEmpty(role) && "用户".equals(role)){
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        }else if(StringUtil.isNotEmpty(role) && "教师".equals(role)){
            params.put("jiaoshiId",request.getSession().getAttribute("userId"));
        }
        params.put("orderBy","id");
        PageUtils page = toupiaoxinxiService.queryPage(params);

        //字典表数据转换
        List<ToupiaoxinxiView> list =(List<ToupiaoxinxiView>)page.getList();
        for(ToupiaoxinxiView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ToupiaoxinxiEntity toupiaoxinxi = toupiaoxinxiService.selectById(id);
        if(toupiaoxinxi !=null){
            //entity转view
            ToupiaoxinxiView view = new ToupiaoxinxiView();
            BeanUtils.copyProperties( toupiaoxinxi , view );//把实体数据重构到view中

            //级联表
            JiaoshiEntity jiaoshi = jiaoshiService.selectById(toupiaoxinxi.getJiaoshiId());
            if(jiaoshi != null){
                BeanUtils.copyProperties( jiaoshi , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                view.setJiaoshiId(jiaoshi.getId());
            }
            //级联表
            ShejitimuEntity shejitimu = shejitimuService.selectById(toupiaoxinxi.getShejitimuId());
            if(shejitimu != null){
                BeanUtils.copyProperties( shejitimu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                view.setShejitimuId(shejitimu.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ToupiaoxinxiEntity toupiaoxinxi, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,toupiaoxinxi:{}",this.getClass().getName(),toupiaoxinxi.toString());
        Wrapper<ToupiaoxinxiEntity> queryWrapper = new EntityWrapper<ToupiaoxinxiEntity>()
            .eq("shejitimu_id", toupiaoxinxi.getShejitimuId())
            .eq("jiaoshi_id", toupiaoxinxi.getJiaoshiId())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ToupiaoxinxiEntity toupiaoxinxiEntity = toupiaoxinxiService.selectOne(queryWrapper);
        if(toupiaoxinxiEntity==null){
            toupiaoxinxi.setInsertTime(new Date());
            toupiaoxinxi.setCreateTime(new Date());
        //  String role = String.valueOf(request.getSession().getAttribute("role"));
        //  if("".equals(role)){
        //      toupiaoxinxi.set
        //  }
            toupiaoxinxiService.insert(toupiaoxinxi);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ToupiaoxinxiEntity toupiaoxinxi, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,toupiaoxinxi:{}",this.getClass().getName(),toupiaoxinxi.toString());
        //根据字段查询是否有相同数据
        Wrapper<ToupiaoxinxiEntity> queryWrapper = new EntityWrapper<ToupiaoxinxiEntity>()
            .notIn("id",toupiaoxinxi.getId())
            .andNew()
            .eq("shejitimu_id", toupiaoxinxi.getShejitimuId())
            .eq("jiaoshi_id", toupiaoxinxi.getJiaoshiId())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ToupiaoxinxiEntity toupiaoxinxiEntity = toupiaoxinxiService.selectOne(queryWrapper);
        if(toupiaoxinxiEntity==null){
            //  String role = String.valueOf(request.getSession().getAttribute("role"));
            //  if("".equals(role)){
            //      toupiaoxinxi.set
            //  }
            toupiaoxinxiService.updateById(toupiaoxinxi);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        toupiaoxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }



    /**
    * 前端列表
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isNotEmpty(role) && "用户".equals(role)){
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        }else if(StringUtil.isNotEmpty(role) && "教师".equals(role)){
            params.put("jiaoshiId",request.getSession().getAttribute("userId"));
        }
        // 没有指定排序字段就默认id倒序
        if(StringUtil.isEmpty(String.valueOf(params.get("orderBy")))){
            params.put("orderBy","id");
        }
        PageUtils page = toupiaoxinxiService.queryPage(params);

        //字典表数据转换
        List<ToupiaoxinxiView> list =(List<ToupiaoxinxiView>)page.getList();
        for(ToupiaoxinxiView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c);
        }
        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ToupiaoxinxiEntity toupiaoxinxi = toupiaoxinxiService.selectById(id);
            if(toupiaoxinxi !=null){
                //entity转view
        ToupiaoxinxiView view = new ToupiaoxinxiView();
                BeanUtils.copyProperties( toupiaoxinxi , view );//把实体数据重构到view中

                //级联表
                    JiaoshiEntity jiaoshi = jiaoshiService.selectById(toupiaoxinxi.getJiaoshiId());
                if(jiaoshi != null){
                    BeanUtils.copyProperties( jiaoshi , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setJiaoshiId(jiaoshi.getId());
                }
                //级联表
                    ShejitimuEntity shejitimu = shejitimuService.selectById(toupiaoxinxi.getShejitimuId());
                if(shejitimu != null){
                    BeanUtils.copyProperties( shejitimu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setShejitimuId(shejitimu.getId());
                }
                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody ToupiaoxinxiEntity toupiaoxinxi, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,toupiaoxinxi:{}",this.getClass().getName(),toupiaoxinxi.toString());
        Wrapper<ToupiaoxinxiEntity> queryWrapper = new EntityWrapper<ToupiaoxinxiEntity>()
            .eq("shejitimu_id", toupiaoxinxi.getShejitimuId())
            .eq("jiaoshi_id", toupiaoxinxi.getJiaoshiId())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
    ToupiaoxinxiEntity toupiaoxinxiEntity = toupiaoxinxiService.selectOne(queryWrapper);
        if(toupiaoxinxiEntity==null){
            toupiaoxinxi.setInsertTime(new Date());
            toupiaoxinxi.setCreateTime(new Date());
        //  String role = String.valueOf(request.getSession().getAttribute("role"));
        //  if("".equals(role)){
        //      toupiaoxinxi.set
        //  }
        toupiaoxinxiService.insert(toupiaoxinxi);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }





}

