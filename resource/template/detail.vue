<template>
    <div>

      <el-container>
        <el-header>
          <el-row>
            <el-col :span="16" :offset="4" class="main-view-container-header">
                <span>用户详情</span>
                <el-button class="filter-item" style="float: right" @click="$router.go(-1)" plain size="mini" icon="el-icon-back">返回</el-button>
            </el-col>
          </el-row>

        </el-header>

        <el-main>
          <el-row>
              <el-col :span="3" :offset="4" >
                <div class="col" >
                    <img :src="qiniuUtil.formatImgKey(userInfo.avatarUrl)" :onerror="qiniuUtil.errorImg()" class="img_css">
                    <p style="text-align: center;margin: 5px">{{ userInfo.wechat }}</p>
                    <div style="height: 20px;text-align: center" v-if="userInfo.status != null">
                      <el-button type="danger" size="mini" round><span v-text="statusFormatTime(userInfo.status)"></span></el-button>
                    </div>
                </div>
              </el-col>
              <el-col :span="13">

                  <el-row>
                    <el-col :span="4" class="listBkg" >真实姓名</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.name }}</el-col>
                    <el-col :span="4" class="listBkg"  >昵称</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.nickname }}</el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="4" class="listBkg" >性别</el-col>
                    <el-col :span="8" class="list" ><span v-text="sexFormatTime(userInfo.sex)"></span></el-col>
                    <el-col :span="4" class="listBkg" >民族</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.nation }}</el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="4" class="listBkg" >生日</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.birthday }}</el-col>
                    <el-col :span="4" class="listBkg">紧急联系人姓名</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.emergencyContact }}</el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="4" class="listBkg" >身高</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.height}}</el-col>
                    <el-col :span="4" class="listBkg">紧急联系人电话</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.emergencyContactMobile }}</el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="4" class="listBkg" >手机号</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.mobile }}</el-col>
                    <el-col :span="4" class="listBkg">地址</el-col>
                    <el-col :span="8" class="list" >{{ userInfo.address }}</el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="4" class="listBkg" style="border-bottom:1px rgba(228, 228, 228, 1) solid">信用分</el-col>
                    <el-col :span="8" class="list" style="border-bottom:1px rgba(228, 228, 228, 1) solid">{{ userInfo.creditScore }}</el-col>
                    <el-col :span="4" class="listBkg" style="border-bottom:1px rgba(228, 228, 228, 1) solid">注册时间</el-col>
                    <el-col :span="8" class="list" style="border-bottom:1px rgba(228, 228, 228, 1) solid">{{ userInfo.createDate | formatTime }}</el-col>
                  </el-row>
              </el-col>
          </el-row>

          <el-row>
            <el-col :span="16" :offset="4" style="margin-top: 50px;border: 1px solid rgb(228, 228, 228);background-color: white">
              <div style="padding: 20px">
                <svg-icon icon-class="标签" ></svg-icon><span>身份证</span>
              </div>
              <el-row style="height: 300px;width: 100%">
                <el-col :span="11" style="height: 300px">
                  <img :src="qiniuUtil.formatImgKey(userInfo.idCardPositive)" :onerror="qiniuUtil.errorImg()" class="idCardZ">
                  <p style="text-align: center;margin-left:15%">正面</p>
                </el-col>
                <el-col :span="10" style="height: 300px">
                  <img :src="qiniuUtil.formatImgKey(userInfo.idCardOpposite)" :onerror="qiniuUtil.errorImg()" class="idCardF">
                  <p style="text-align: center;margin-left:24%">反面</p>
                </el-col>
              </el-row>
              <div style="padding: 20px">
                <svg-icon icon-class="标签" ></svg-icon><span>健康证</span>
              </div>
              <el-row style=" height: 300px;width: 100%">
                <el-col :span="11" style="height: 300px">
                  <img :src="qiniuUtil.formatImgKey(userInfo.healthCertificatePositive)" :onerror="qiniuUtil.errorImg()" class="idCardZ">
                  <p style="text-align: center;margin-left:15%">正面</p>
                </el-col>
                <el-col :span="10" style="height: 300px">
                  <img :src="qiniuUtil.formatImgKey(userInfo.healthCertificateOpposite)" :onerror="qiniuUtil.errorImg()" class="idCardF">
                  <p style="text-align: center;margin-left:24%">反面</p>
                </el-col>
              </el-row>
            </el-col>
          </el-row>

          <el-row>
              <el-col :span="2" :offset="4">
                <div style="height: 250px">
                  <div style="margin-top: 20px;text-align: center">
                    <span><span style="color: red">*</span>企业黑名单：</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="10">
                <div style="margin-top: 10px">
                  <el-table :data="backList" max-height="400" border style="width: 100%" >
                    <el-table-column label="企业" align="center">
                      <template slot-scope="scope">
                        {{ scope.row.companyName }}
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center">
                      <template slot-scope="scope">
                        <el-button @click="deletedCompany(scope.row.id,scope.row.companyId)" type="text" size="small">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-col>
              <el-col :span="1">
                <div style="margin-top: 20px;margin-left: 20px">
                  <el-button type="primary" @click="queryCompanyList()" size="mini" round>添加</el-button>
                </div>
              </el-col>
          </el-row>

        </el-main>

      </el-container>

      <el-dialog :title="dialogTitle"  :visible.sync="dialogFormVisible">

        <el-input @keyup.enter.native="queryCompanyList" style="width: 200px;margin-bottom: 10px" clearable class="filter-item" placeholder="请输企业名称" v-model="query.companyName"></el-input>
        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="search">搜索</el-button>
        <el-button class="filter-item" type="primary" v-waves icon="el-icon-refresh" @click="searchQuery">重置</el-button>
        <el-button class="filter-item" type="primary" v-waves icon="el-icon-plus" style="margin-left: 40%" @click="saveCompanys">添加</el-button>
        <el-button @click="$router.go(0)">取消</el-button>

        <el-table :data="companyList"  border height=550 style="width: 100%" @selection-change="selectionChange" ref="companyTable">
          <el-table-column type="selection" width="55" fixed="left"></el-table-column>
          <el-table-column label="企业名称" align="center">
            <template slot-scope="scope">
              {{ scope.row.companyName }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button @click="saveOneCompany(scope.row.companyId)" type="text" size="small">添加</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
    </div>
</template>

<script>
  import { getParttimeUser } from '@/api/function'
  import request from '@/utils/request'
  import { parseTime } from '@/utils/index'
  import qiniuUtil from '@/utils/qiniu'

  export default {
    data() {
      return {
        id: null,
        dialogFormVisible:false,
        dialogTitle:'',
        userInfo: {
          wechat:'',
          name: '',
          nickname:'',
          mobile:'',
          sex:'',
          nation:'',
          birthday:'',
          height:'',
          idCardPositive:'',
          idCardOpposite:'',
          emergencyContact:'',
          emergencyContactMobile:'',
          address:'',
          createDate:'',
          healthCertificatePositive:'',
          healthCertificateOpposite:'',
          status:'',
          avatarUrl:'',
          creditScore: ''
        },
        backList:[],
        companyList:[],
        companyIds:[],
        companyCheckd:[],
        query:{
          companyName:''
        },
        qiniuUtil
      };
    },
    methods: {
      sexFormatTime:function (temp) {
        if (temp == 1) {
          return '男';
        } else {
          return '女';
        }
      },
      statusFormatTime:function (temp) {
        if (temp == 1) {
          return '审核中';
        } else if(temp == 2) {
          return '审核通过';
        }else if(temp == 3) {
          return '审核未通过';
        }else if(temp == 4){
          return '待审核';
        }
      },
      deletedCompany:function (id,companyId) {
        if(id != null && companyId != null){
          const params = {
            userId : id,
            companyId : companyId
          }
          request({
            url:'/parttimeUser/getbackListDelete',
            method:'get',
            params
          }).then(response =>{
            this.$message(response.message)
            this.queryBackList()
          })
        }

      },
      queryBackList(){
        request({
          url:'/parttimeUser/getBackList/'+this.id,
          method:'get',
        }).then(response =>{
          this.backList = response.data
        })
      },
      queryCompanyList(){
        this.dialogFormVisible = true
        this.dialogTitle = '添加企业黑名单'
        const params = {
          companyName: this.query.companyName
        }
        request({
          url:'/parttimeUser/getCompanyInfo/'+this.id,
          method: 'get',
          params
        }).then(response => {
          this.companyList = response.data
          console.log(this.companyCheckd)
        })
      },
      selectionChange(data){
        let ids = []

        for (let i = 0 ; i < data.length;i++){
          ids.push(data[i].companyId)
        }
        this.companyIds = ids;
        // this.companyCheckd.data
        console.log(this.companyIds)
      },
      search() {
        this.queryCompanyList()
      },
      searchQuery() {
        this.query.companyName = ''
        this.queryCompanyList()
      },
      saveCompanys() {
        const params = {
          id : this.id,
          ids : this.companyIds.join(",")
        }
        request({
          url:'/parttimeUser/saveBackListCompany',
          method: 'get',
          params
        }).then(response => {
          this.dialogFormVisible = false
          this.queryBackList()
        })
      },
      saveOneCompany(companyId) {
        const params = {
          id : this.id,
          companyId : companyId
        }
        request({
          url:'/parttimeUser/saveBackList',
          method: 'get',
          params
        }).then(response => {
          this.dialogFormVisible = false
          this.queryBackList()
        })
      }

    },
    created() {
      this.id = this.$route.query.id;
      if (this.id) {
        getParttimeUser(this.id).then(res =>{
          let _userInfo = this.userInfo
          const data = res.data
          _userInfo.wechat = data.wechat
          _userInfo.name = data.name
          _userInfo.nickname = data.nickname
          _userInfo.mobile = data.mobile
          _userInfo.sex = data.sex
          _userInfo.nation = data.nation
          _userInfo.birthday = data.birthday
          _userInfo.idCardPositive = data.idCardPositive
          _userInfo.idCardOpposite = data.idCardOpposite
          _userInfo.emergencyContact = data.emergencyContact
          _userInfo.emergencyContactMobile = data.emergencyContactMobile
          _userInfo.address = data.address
          _userInfo.height = data.height == null? '' : data.height+ 'cm'
          _userInfo.createDate = data.createDate
          _userInfo.status = data.status
          _userInfo.healthCertificatePositive = data.healthCertificatePositive
          _userInfo.healthCertificateOpposite = data.healthCertificateOpposite
          _userInfo.avatarUrl = data.avatarUrl
          _userInfo.creditScore = data.creditScore
        })
        this.queryBackList()
      }
    },
    filters: {
      formatTime(time) {
        if (time !=null){
          return parseTime(time)
        }
      }
    },
  }
</script>

<style scoped>
  .listBkg{
    background-color: rgba(249, 250, 252, 1);
    height: 45px;
    line-height: 45px;
    text-align: center;
    border: 1px rgba(228, 228, 228, 1) solid;
    font-family: '微软雅黑';
    font-size: 13px;
    line-height: 45px;
    border-bottom: none;
    border-left: none
  }
  .list{
    height: 45px;
    line-height: 45px;
    text-align: center;
    border-top:1px rgba(228, 228, 228, 1) solid;
    background-color: rgba(255, 255, 255, 1);
    border-right:1px rgba(228, 228, 228, 1) solid
  }
  .col{
    background-color: rgb(255, 255, 255);height: 270px;border: 1px rgba(228, 228, 228, 1) solid
  }
  .img_css{
    height: 80px;
    width: 80px;
    border-radius:100%;
    margin-left: 32%;
    margin-top: 21%;
  }
  .idCardZ{
    height: 60%;
    width: 65%;
    margin-left: 25%;
    border-radius: 20px;
  }
  .idCardF{
    height: 60%;
    width: 71.5%;
    margin-left: 25%;
    border-radius: 20px;
  }

</style>
