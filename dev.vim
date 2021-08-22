" What do you mean you don't use vim?

function! FlinklabRunExample()
  call FlinklabTermSendKeysNormal()
endfunction

function! FlinklabTermSendKeysNormal()
  call term_sendkeys('flinklab_term', "cd ~/flinklab; ./dev.sh " . expand('%:h') . " \r")
endfunction

function! FlinklabTerm()
  botright split
  call term_start('zsh --login', {'curwin' : 1, 'term_finish' : 'close', 'term_name': 'flinklab_term'})
  wincmd p
endfunction

function! FlinklabWindowSetup()
  cd ~/flinklab
  call FlinklabTerm()
  NERDTree
endfunction

function! FlinklabOtherSetup()
  map <c-b> :w \| call FlinklabRunExample() <cr>
endfunction

function! Flinklab()
  call FlinklabWindowSetup()
  call FlinklabOtherSetup()
endfunction

command! Flinklab call Flinklab()
cabbrev flil <c-r>=(getcmdtype()==':' && getcmdpos()==1 ? 'call Flinklab()' : 'flil')<CR>
