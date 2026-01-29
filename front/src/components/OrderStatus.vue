<template>
  <div class="order-status">
    <div 
      class="status-badge"
      :class="{
        'status-pending': status === '待处理',
        'status-delivering': status === '配送中',
        'status-completed': status === '已完成',
        'status-cancelled': status === '已取消'
      }"
    >
      {{ status }}
    </div>
    
    <div class="status-timeline" v-if="showTimeline">
      <div class="timeline-item" :class="{ active: isStepActive('待处理') }">
        <div class="timeline-dot"></div>
        <div class="timeline-text">待处理</div>
      </div>
      
      <div class="timeline-item" :class="{ active: isStepActive('配送中') }">
        <div class="timeline-dot"></div>
        <div class="timeline-text">配送中</div>
      </div>
      
      <div class="timeline-item" :class="{ active: isStepActive('已完成') }">
        <div class="timeline-dot"></div>
        <div class="timeline-text">已完成</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrderStatus',
  props: {
    status: {
      type: String,
      required: true,
      validator: value => ['待处理', '配送中', '已完成', '已取消'].includes(value)
    },
    showTimeline: {
      type: Boolean,
      default: false
    }
  },
  setup(props) {
    const statusOrder = ['待处理', '配送中', '已完成'];
    
    const isStepActive = (step) => {
      if (props.status === '已取消') {
        return false;
      }
      
      const currentIndex = statusOrder.indexOf(props.status);
      const stepIndex = statusOrder.indexOf(step);
      
      return stepIndex <= currentIndex;
    };
    
    return {
      isStepActive
    };
  }
};
</script>

<style scoped>
.order-status {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.status-badge {
  display: inline-block;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 15px;
}

.status-pending {
  background-color: #2196f3;
  color: white;
}

.status-delivering {
  background-color: #ff9800;
  color: white;
}

.status-completed {
  background-color: #4caf50;
  color: white;
}

.status-cancelled {
  background-color: #9e9e9e;
  color: white;
}

.status-timeline {
  display: flex;
  width: 100%;
  margin-top: 10px;
  position: relative;
}

.status-timeline:before {
  content: '';
  position: absolute;
  top: 8px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #e0e0e0;
  z-index: 1;
}

.timeline-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 2;
}

.timeline-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background-color: #e0e0e0;
  margin-bottom: 8px;
}

.timeline-text {
  font-size: 14px;
  color: #9e9e9e;
}

.timeline-item.active .timeline-dot {
  background-color: var(--primary-color, #e53935);
}

.timeline-item.active .timeline-text {
  color: var(--primary-color, #e53935);
  font-weight: bold;
}
</style> 