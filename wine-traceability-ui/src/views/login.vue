<template>
  <div class="auth-page login-page">
    <div class="auth-shell">
      <section class="auth-intro">
        <p class="auth-kicker">Ningxia Wine Traceability</p>
        <h1>宁夏葡萄酒溯源系统</h1>
        <p class="auth-desc">
          以产区、地块、批次、容器、灌装和仓储物流为主线，构建一瓶酒从生产到消费的全链路数字化追溯平台。
        </p>
        <div class="auth-points">
          <div class="point-item">
            <span class="point-index">01</span>
            <div>
              <div class="point-title">生产过程留痕</div>
              <div class="point-text">记录车间、工序、容器流转和质检节点。</div>
            </div>
          </div>
          <div class="point-item">
            <span class="point-index">02</span>
            <div>
              <div class="point-title">扫码溯源展示</div>
              <div class="point-text">通过时间线和图表快速还原单瓶葡萄酒旅程。</div>
            </div>
          </div>
          <div class="point-item">
            <span class="point-index">03</span>
            <div>
              <div class="point-title">统计监控闭环</div>
              <div class="point-text">聚合查询热度、评分反馈与异常线索，支撑管理决策。</div>
            </div>
          </div>
        </div>
      </section>

      <section class="auth-panel">
        <div class="brand-chip">管理端登录</div>
        <h3 class="auth-title">欢迎进入系统</h3>
        <p class="auth-subtitle">请输入账号、密码和验证码完成登录。</p>

        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              type="text"
              auto-complete="off"
              placeholder="请输入账号"
            >
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              auto-complete="off"
              placeholder="请输入密码"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="code-row">
              <el-input
                v-model="loginForm.code"
                auto-complete="off"
                placeholder="请输入验证码"
                @keyup.enter.native="handleLogin"
              >
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img"/>
              </div>
            </div>
          </el-form-item>
          <div class="auth-actions">
            <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
            <router-link v-if="register" class="auth-link" :to="'/register'">立即注册</router-link>
          </div>
          <el-form-item>
            <el-button
              :loading="loading"
              size="medium"
              type="primary"
              class="submit-btn"
              @click.native.prevent="handleLogin"
            >
              <span v-if="!loading">登录系统</span>
              <span v-else>正在登录...</span>
            </el-button>
          </el-form-item>
        </el-form>

        <div class="auth-note">
          <span>演示账号：</span>
          <strong>admin</strong>
          <span>/</span>
          <strong>admin123</strong>
        </div>
      </section>
    </div>

    <div class="auth-footer">Copyright © 2026 宁夏葡萄酒溯源系统</div>
  </div>
</template>

<script>
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: 'Login',
  data() {
    return {
      codeUrl: '',
      loginForm: {
        username: 'admin',
        password: 'admin123',
        rememberMe: false,
        code: '',
        uuid: ''
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入账号' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入密码' }
        ],
        code: [
          { required: true, trigger: 'change', message: '请输入验证码' }
        ]
      },
      loading: false,
      captchaEnabled: true,
      register: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get('username')
      const password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
        code: this.loginForm.code,
        uuid: this.loginForm.uuid
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set('username', this.loginForm.username, { expires: 30 })
            Cookies.set('password', encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch('Login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' }).catch(() => {})
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

.login-form ::v-deep .el-input__inner {
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

.login-code {
  height: 44px;
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid #eadfd5;
  background: #fff;
}

.login-code-img {
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
  color: #7d675d;
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

.auth-note {
  margin-top: 8px;
  padding: 14px 16px;
  border-radius: 14px;
  background: linear-gradient(180deg, #fff7ef, #f9efe6);
  border: 1px solid #ecdccd;
  color: #765f54;
  font-size: 13px;
}

.auth-note strong {
  color: #4b3127;
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
