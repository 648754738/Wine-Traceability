<template>
  <div class="sidebar-logo-container" :class="{ collapse: collapse }" :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link is-collapse" to="/">
        <div class="brand-copy brand-copy--collapse">
          <div class="brand-title brand-title--collapse">管理系统</div>
        </div>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <div class="brand-copy">
          <div class="brand-title">宁夏葡萄酒管理系统</div>
        </div>
      </router-link>
    </transition>
  </div>
</template>

<script>
import variables from '@/assets/styles/variables.scss'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    variables() {
      return variables
    },
    sideTheme() {
      return this.$store.state.settings.sideTheme
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.2s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8px;
  overflow: hidden;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.015), rgba(255, 255, 255, 0)),
    linear-gradient(180deg, rgba(124, 45, 45, 0.12), rgba(124, 45, 45, 0.03));

  .sidebar-logo-link {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
  }

  .sidebar-logo-link.is-collapse {
    justify-content: center;
  }
}

.brand-copy {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  min-width: 0;
  height: 100%;
  line-height: 1.1;
  text-align: center;
}

.brand-copy--collapse {
  align-items: center;
}

.brand-title {
  color: #fff7f1;
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 0.03em;
  white-space: nowrap;
}

.brand-title--collapse {
  font-size: 12px;
  letter-spacing: 0.08em;
}
</style>
