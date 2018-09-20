
  Pod::Spec.new do |s|
    s.name = 'TeamhiveCapacitorEmail'
    s.version = '0.0.1'
    s.summary = 'Email composer'
    s.license = 'GPL'
    s.homepage = 'https://github.com/TeamHive/capacitor-email'
    s.author = 'TeamHive'
    s.source = { :git => 'https://github.com/TeamHive/capacitor-email', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '10.0'
    s.dependency 'Capacitor'
  end