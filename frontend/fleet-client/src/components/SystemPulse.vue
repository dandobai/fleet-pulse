<template>
    <svg width="100" height="30" viewBox="0 0 100 30" class="overflow-visible">
        <defs>
            <mask id="pulseMask">
                <rect x="0" y="0" width="100" height="30" fill="white" />
            </mask>
        </defs>

        <polyline fill="none" stroke="#22c55e" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
            points="0,15 10,15 15,5 20,25 25,15 40,15 45,10 50,20 55,15 100,15" class="pulse-line"
            mask="url(#pulseMask)" />
    </svg>
</template>

<style scoped>
.pulse-line {

    stroke-dasharray: 10 200;
    animation: pulse-anim 2s linear infinite;
}

@keyframes pulse-anim {
    from {
        stroke-dashoffset: 0;
    }

    to {
        stroke-dashoffset: -200;
    }
}
</style>