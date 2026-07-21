import { ref, computed, type Ref } from 'vue';

export function usePaginationScroll<T>(items: Ref<T[]>, initialLimit = 50, step = 50) {
    const displayLimit = ref(initialLimit);

    const visibleItems = computed(() => {
        return items.value.slice(0, displayLimit.value);
    });

    const handleScroll = (e: Event) => {
        const target = e.target as HTMLElement;
        const isAtBottom = target.scrollTop + target.clientHeight >= target.scrollHeight - 30;

        if (isAtBottom && displayLimit.value < items.value.length) {
            displayLimit.value += step;
        }
    };

    return {
        visibleItems,
        handleScroll
    };
}