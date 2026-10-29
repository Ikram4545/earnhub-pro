<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');

// Secret verification key
$VALID_SECRET = "ehp_live_99a";

// Get query parameters or POST data
$userId = $_REQUEST['user_id'] ?? $_REQUEST['subid'] ?? $_REQUEST['uid'] ?? 'guest_user';
$reward = (int)($_REQUEST['reward'] ?? $_REQUEST['payout'] ?? $_REQUEST['amount'] ?? 0);
$secret = $_REQUEST['secret'] ?? '';
$offerName = $_REQUEST['offer_name'] ?? $_REQUEST['campaign'] ?? 'Offer Task';
$network = $_REQUEST['network'] ?? 'CPA Partner';
$txId = $_REQUEST['tx_id'] ?? $_REQUEST['transaction_id'] ?? uniqid('tx_');

// Check secret if supplied
$isVerified = ($secret === $VALID_SECRET || empty($secret)); // Some networks do IP whitelist instead of token

$logEntry = [
    'timestamp' => date('Y-m-d H:i:s'),
    'user_id' => $userId,
    'reward' => $reward,
    'offer_name' => $offerName,
    'network' => $network,
    'tx_id' => $txId,
    'verified' => $isVerified,
    'ip' => $_SERVER['REMOTE_ADDR'] ?? '0.0.0.0'
];

// Append to log file
$logFile = __DIR__ . '/postbacks.log';
file_put_contents($logFile, json_encode($logEntry) . PHP_EOL, FILE_APPEND | LOCK_EX);

// Update user balances file
$balancesFile = __DIR__ . '/user_balances.json';
$balances = [];
if (file_exists($balancesFile)) {
    $balances = json_decode(file_get_contents($balancesFile), true) ?: [];
}

if (!isset($balances[$userId])) {
    $balances[$userId] = [
        'coins' => 1000, // starting demo balance
        'total_earned' => 0,
        'completed_offers' => []
    ];
}

$balances[$userId]['coins'] += $reward;
$balances[$userId]['total_earned'] += $reward;
$balances[$userId]['completed_offers'][] = [
    'tx_id' => $txId,
    'offer' => $offerName,
    'reward' => $reward,
    'date' => date('Y-m-d H:i')
];

file_put_contents($balancesFile, json_encode($balances, JSON_PRETTY_PRINT), LOCK_EX);

echo json_encode([
    'status' => 'success',
    'code' => 200,
    'message' => 'Postback received and credited successfully',
    'data' => [
        'user_id' => $userId,
        'credited_coins' => $reward,
        'new_balance' => $balances[$userId]['coins'],
        'tx_id' => $txId
    ]
]);
