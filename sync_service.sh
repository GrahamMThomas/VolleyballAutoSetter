ssh pi@cassette.local "rm -rf VolleyballAutoSetter"
rsync -ar ./pi_service/ pi@cassette.local:VolleyballAutoSetter
ssh pi@cassette.local "pip install -r VolleyballAutoSetter/requirements.txt"
ssh pi@cassette.local "cd VolleyballAutoSetter && python3 main.py"