import pygatt

# The BGAPI backend will attemt to auto-discover the serial device name of the
# attached BGAPI-compatible USB adapter.

# adapter = pygatt.BGAPIBackend(serial_port="COM3")
adapter = pygatt.BGAPIBackend()
print adapter._serial_port

# device = adapter.connect('01:23:45:67:89:ab')
adapter.start()
# v = adapter.scan(1000)
# device = adapter.connect('EAE6C348941C6F02A52596B16AE69A09')
# val = device.char_read('7A2140F6-24D0-681E-1238-A1519C1A396B')
# # device = adapter.connect('7A2140F6-24D0-681E-1238-A1519C1A396B')
#
