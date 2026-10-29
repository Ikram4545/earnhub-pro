import os
import sys
from http.server import SimpleHTTPRequestHandler, HTTPServer
import urllib.parse
import json

PORT = 8080
DIRECTORY = os.path.dirname(os.path.abspath(__file__))

class EarnHubHTTPHandler(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def do_GET(self):
        parsed = urllib.parse.urlparse(self.path)
        
        # Postback Webhook Endpoint Simulation
        if parsed.path == '/api/postback':
            params = urllib.parse.parse_qs(parsed.query)
            user_id = params.get('user_id', ['unknown'])[0]
            reward = params.get('reward', ['0'])[0]
            secret = params.get('secret', ['none'])[0]

            response_data = {
                "status": "success",
                "code": 200,
                "message": f"Postback received successfully for user {user_id}",
                "data": {
                    "user_id": user_id,
                    "reward_credited": reward,
                    "secret_verified": secret == "ehp_live_99a",
                    "timestamp": "2026-10-29T02:00:00Z"
                }
            }

            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.send_header('Access-Control-Allow-Origin', '*')
            self.end_headers()
            self.wfile.write(json.dumps(response_data, indent=2).encode('utf-8'))
            print(f"[POSTBACK RECEIVED] User: {user_id} | Reward: {reward} Coins | Verified: {secret == 'ehp_live_99a'}")
            return

        # Default static file serving
        return super().do_GET()

def run_server():
    server_address = ('', PORT)
    httpd = HTTPServer(server_address, EarnHubHTTPHandler)
    print(f"==================================================")
    print(f"  EarnHub Pro Local Server Running")
    print(f"  App URL:       http://localhost:{PORT}/")
    print(f"  Prototype URL: http://localhost:{PORT}/prototype.html")
    print(f"  Postback API:  http://localhost:{PORT}/api/postback?user_id=USER-8819&reward=1200&secret=ehp_live_99a")
    print(f"==================================================")
    sys.stdout.flush()
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nStopping server...")
        httpd.server_close()

if __name__ == '__main__':
    run_server()