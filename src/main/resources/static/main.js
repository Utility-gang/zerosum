document.addEventListener('input', function (event) {
    if (event.target.classList.contains('buy-amount-input')) {
        const input = event.target;

//        get the current price and the id of the span to target
        const price = parseFloat(input.getAttribute('data-curr-price'));
        const targetId = input.getAttribute('data-target-id');
        const amount = parseFloat(input.value) || 0;

        const total = (amount * price).toFixed(2);

//        protect against no stock price or no value given
        if (isNaN(total)) {
            total = 0.00
        }

//        update the 'cost' span to show the total cost
        document.getElementById(targetId).innerText = total;
    }
})