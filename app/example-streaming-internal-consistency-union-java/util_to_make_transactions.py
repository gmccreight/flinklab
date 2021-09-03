#!/usr/bin/env python3

# This does not need to be run every time...
# We just used it to generate the transactions/1 file locally, which is copied
# to /tmp/transactions/1 on flinklab_taskmanager_1 as part of running 000_all.sh

import random
import json

random.seed(42)
max_id = 1000
for id in range(0,max_id):
    second = ((60 * id) // max_id)
    from_account = id % 10
    to_account = (from_account + 1) % 10
    row = json.dumps({
        'id': id,
        'from_account': from_account,
        'to_account': to_account,
        'amount': 1,
        'ts': f'2021-01-01 00:00:{second:02d}.000',
    })
    print(f'{row}')
