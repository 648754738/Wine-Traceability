<template>
  <div class="auth-page register-page">
    <div class="auth-shell">
      <section class="auth-intro">
        <p class="auth-kicker">Ningxia Wine Traceability</p>
        <h1>创建系统账号</h1>
        <p class="auth-desc">
          新用户注册后即可进入宁夏葡萄酒溯源系统，参与基础信息管理、生产过程记录、质检灌装和消费者反馈闭环。
        </p>
        <div class="auth-points">
          <div class="point-item">
            <span class="point-index">01</span>
            <div>
              <div class="point-title">账号统一管理</div>
              <div class="point-text">按角色和所属酒庄进行权限控制与业务分配。</div>
            </div>
          </div>
          <div class="point-item">
            <span class="point-index">02</span>
            <div>
              <div class="point-title">追溯链路协同</div>
              <div class="point-text">支持生产、仓储、质检、查看等不同岗位协同录入。</div>
            </div>
          </div>
          <div class="point-item">
            <span class="point-index">03</span>
            <div>
              <div class="point-title">品牌化展示</div>
              <div class="point-text">系统入口、总览看板与溯源查询保持统一视觉风格。</div>
            </div>
          </div>
        </div>
      </section>

      <section class="auth-panel">
        <div class="brand-chip">新用户注册</div>
        <h3 class="auth-title">创建账号</h3>
        <p class="auth-subtitle">请填写账号信息并完成验证码校验。</p>

        <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
          <el-form-item prop="username">
            <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="请输入账号">
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              auto-complete="off"
              placeholder="请输入密码"
              @keyup.enter.native="handleRegister"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              auto-complete="off"
              placeholder="请再次输入密码"
              @keyup.enter.native="handleRegister"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="code-row">
              <el-input
                v-model="registerForm.code"
                auto-complete="off"
                placeholder="请输入验证码"
                @keyup.enter.native="handleRegister"
              >
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <div class="register-code">
                <img :src="codeUrl" @click="getCode" class="register-code-img"/>
              </div>
            </div>
          </el-form-item>
          <div class="auth-actions auth-actions-right">
            <router-link class="auth-link" :to="'/login'">使用已有账号登录</router-link>
          </div>
          <el-form-item>
            <el-button
              :loading="loading"
              size="medium"
              type="primary"
              class="submit-btn"
              @click.native.prevent="handleRegister"
            >
              <span v-if="!loading">注册账号</span>
              <span v-else>正在注册...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </section>
    </div>

    <div class="auth-footer">Copyright © 2026 宁夏葡萄酒溯源系统</div>
  </div>
</template>

<script>
import { getCodeImg, register } from '@/api/login'

export default {
  name: 'Register',
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      codeUrl: '',
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        code: '',
        uuid: ''
      },
      registerRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入账号' },
          { min: 2, max: 20, message: '账号长度必须在 2 到 20 个字符之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入密码' },
          { min: 5, max: 20, message: '密码长度必须在 5 到 20 个字符之间', trigger: 'blur' },
          { pattern: /^[^<>"'|\\]+$/, message: '密码不能包含非法字符 < > " \' | \\', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: 'blur', message: '请再次输入密码' },
          { required: true, validator: equalToPassword, trigger: 'blur' }
        ],
        code: [
          { required: true, trigger: 'change', message: '请输入验证码' }
        ]
      },
      loading: false,
      captchaEnabled: true
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img
          this.registerForm.uuid = res.uuid
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          register(this.registerForm).then(() => {
            const username = this.registerForm.username
            this.$alert("<font color='red'>恭喜您，账号 " + username + ' 注册成功。</font>', '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push('/login')
            }).catch(() => {})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.auth-page {
  min-height: 100vh;
  padding: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background:
    radial-gradient(circle at top right, rgba(200, 125, 58, 0.18), transparent 24%),
    radial-gradient(circle at left bottom, rgba(124, 45, 45, 0.2), transparent 28%),
    linear-gradient(135deg, #2c1616 0%, #5c2424 45%, #a24a32 100%);
}

.auth-page::before,
.auth-page::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
}

.auth-page::before {
  width: 420px;
  height: 420px;
  top: -120px;
  right: -80px;
  background: rgba(255, 255, 255, 0.06);
}

.auth-page::after {
  width: 280px;
  height: 280px;
  bottom: -80px;
  left: -40px;
  background: rgba(255, 240, 227, 0.08);
}

.auth-shell {
  width: 100%;
  max-width: 1180px;
  display: grid;
  grid-template-columns: minmax(0, 1.2fr) minmax(380px, 0.8fr);
  gap: 24px;
  align-items: stretch;
  position: relative;
  z-index: 1;
}

.auth-intro,
.auth-panel {
  border-radius: 28px;
  backdrop-filter: blur(10px);
}

.auth-intro {
  padding: 42px;
  color: #fff8f3;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.08), rgba(255, 255, 255, 0.03));
  border: 1px solid rgba(255, 255, 255, 0.12);
  box-shadow: 0 24px 50px rgba(38, 16, 16, 0.22);
}

.auth-kicker {
  margin: 0 0 14px;
  font-size: 12px;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  color: rgba(255, 248, 243, 0.72);
}

.auth-intro h1 {
  margin: 0;
  font-size: 40px;
  line-height: 1.2;
}

.auth-desc {
  margin: 18px 0 28px;
  font-size: 15px;
  line-height: 1.9;
  color: rgba(255, 248, 243, 0.88);
}

.auth-points {
  display: grid;
  gap: 16px;
}

.point-item {
  display: flex;
  gap: 14px;
  padding: 18px 20px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.07);
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.point-index {
  width: 38px;
  height: 38px;
  line-height: 38px;
  text-align: center;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.12);
  font-weight: 700;
  flex-shrink: 0;
}

.point-title {
  margin-bottom: 6px;
  font-size: 15px;
  font-weight: 600;
}

.point-text {
  font-size: 13px;
  line-height: 1.8;
  color: rgba(255, 248, 243, 0.8);
}

.auth-panel {
  padding: 34px 32px 28px;
  background: rgba(255, 252, 248, 0.96);
  box-shadow: 0 24px 50px rgba(38, 16, 16, 0.2);
}

.brand-chip {
  display: inline-flex;
  padding: 7px 14px;
  border-radius: 999px;
  font-size: 12px;
  color: #7c2d2d;
  background: rgba(124, 45, 45, 0.08);
}

.auth-title {
  margin: 16px 0 10px;
  font-size: 28px;
  color: #32211d;
}

.auth-subtitle {
  margin: 0 0 24px;
  font-size: 13px;
  color: #8b756a;
}

.register-form ::v-deep .el-input__inner {
  height: 44px;
  border-radius: 14px;
  border: 1px solid #eadfd5;
  background: #fffdfb;
}

.input-icon {
  height: 44px;
  width: 16px;
  margin-left: 2px;
}

.code-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 118px;
  gap: 12px;
  align-items: center;
}

.register-code {
  height: 44px;
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid #eadfd5;
  background: #fff;
}

.register-code-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
}

.auth-actions {
  margin: 4px 0 22px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.auth-actions-right {
  justify-content: flex-end;
}

.auth-link {
  color: #7c2d2d;
}

.submit-btn {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, #7c2d2d, #a24a32);
  box-shadow: 0 14px 24px rgba(124, 45, 45, 0.18);
}

.auth-footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 14px;
  text-align: center;
  color: rgba(255, 248, 243, 0.72);
  font-size: 12px;
  letter-spacing: 0.08em;
  z-index: 1;
}

@media (max-width: 1024px) {
  .auth-shell {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .auth-page {
    padding: 18px;
  }

  .auth-intro,
  .auth-panel {
    padding: 24px;
  }

  .auth-intro h1 {
    font-size: 30px;
  }

  .code-row {
    grid-template-columns: 1fr;
  }
}
</style>
