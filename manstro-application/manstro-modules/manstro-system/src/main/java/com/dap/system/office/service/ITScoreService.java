package com.dap.system.office.service;

import java.util.List;
import com.dap.system.office.domain.TScore;
import com.dap.system.office.domain.vo.TStudentDetailScoreVo;
import com.dap.system.office.domain.vo.TStudentScoreInfoVo;

/**
 * 分数Service接口
 * 
 * @author dap
 * @date 2021-11-12
 */
public interface ITScoreService 
{
    /**
     * 查询分数
     * 
     * @param id 分数ID
     * @return 分数
     */
    public TStudentDetailScoreVo selectTScoreById(Integer id,Integer semId);

    /**
     * 查询分数列表
     * 
     * @param tScore 分数
     * @return 分数集合
     */
    public List<TScore> selectTScoreList(TScore tScore);



    /**
     * 分页查询分数列表
     *
     * @param tScore 分数
     * @return 分数集合
     */
    public List<TScore> selectTScoreListPage(TScore tScore);


    /**
     * 新增分数
     * 
     * @param tScore 分数
     * @return 结果
     */
    public int insertTScore(TStudentDetailScoreVo tScore);

    /**
     * 修改分数
     * 
     * @param
     * @return 结果
     */
    public int updateTScore(TStudentDetailScoreVo tStudentDetailScoreVo);

    /**
     * 批量删除分数
     * 
     * @param ids 需要删除的分数ID
     * @return 结果
     */
    public int deleteTScoreByIds(Integer ids,Integer semId);

    /**
     * 删除分数信息
     * 
     * @param id 分数ID
     * @return 结果
     */
    public int deleteTScoreById(Integer id);

    List<TStudentScoreInfoVo> selectStudentScoreInfo(TScore tScore);

    List<TStudentDetailScoreVo> selectStudentScoreDetails() ;
}
