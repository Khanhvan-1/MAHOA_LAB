"""LAB 2 - AES-256-CBC, Tkinter. Chạy --self-test để kiểm tra không mở GUI."""
import base64
import secrets
import sys
from pathlib import Path
import tkinter as tk
from tkinter import filedialog, messagebox, ttk
from cryptography.hazmat.primitives import padding
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes


def new_key():
    return base64.b64encode(secrets.token_bytes(32)).decode('ascii')


def new_iv():
    return base64.b64encode(secrets.token_bytes(16)).decode('ascii')


def _key_iv(key_b64, iv_b64):
    try:
        key = base64.b64decode(key_b64.strip(), validate=True)
        iv = base64.b64decode(iv_b64.strip(), validate=True)
    except Exception as ex:
        raise ValueError('Key/IV phải là chuỗi Base64 hợp lệ.') from ex
    if len(key) != 32 or len(iv) != 16:
        raise ValueError('Key phải 32 byte và IV phải 16 byte. Bấm Generate Key / Generate IV.')
    return key, iv


def encrypt_text(plaintext, key_b64, iv_b64):
    key, iv = _key_iv(key_b64, iv_b64)
    padder = padding.PKCS7(128).padder()
    data = padder.update(plaintext.encode('utf-8')) + padder.finalize()
    enc = Cipher(algorithms.AES(key), modes.CBC(iv)).encryptor()
    return base64.b64encode(enc.update(data) + enc.finalize()).decode('ascii')


def decrypt_text(ciphertext_b64, key_b64, iv_b64):
    key, iv = _key_iv(key_b64, iv_b64)
    try:
        data = base64.b64decode(ciphertext_b64.strip(), validate=True)
    except Exception as ex:
        raise ValueError('Ciphertext phải là Base64.') from ex
    if not data or len(data) % 16:
        raise ValueError('Độ dài bản mã CBC phải là bội số của 16 byte.')
    dec = Cipher(algorithms.AES(key), modes.CBC(iv)).decryptor()
    padded = dec.update(data) + dec.finalize()
    try:
        unpadder = padding.PKCS7(128).unpadder()
        return (unpadder.update(padded) + unpadder.finalize()).decode('utf-8')
    except Exception as ex:
        raise ValueError('Giải mã thất bại: sai Key/IV hoặc bản mã bị sửa.') from ex


def to_file(iv, ciphertext):
    return 'IV=' + iv.strip() + '\n' + ciphertext.strip() + '\n'


def from_file(content):
    lines = content.strip().split('\n', 1)
    if len(lines) != 2 or not lines[0].startswith('IV='):
        raise ValueError('Tệp không đúng định dạng: dòng 1 IV=..., dòng 2 bản mã.')
    return lines[0][3:].strip(), lines[1].strip()


class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title('AES-CBC Python - LAB 2')
        f = ttk.Frame(self, padding=14)
        f.grid(sticky='nsew')
        self.columnconfigure(0, weight=1)
        self.rowconfigure(0, weight=1)
        f.columnconfigure(1, weight=1)
        ttk.Label(f, text='BÀI NÂNG CAO 02 - AES-256-CBC (PYTHON)', font=('Segoe UI',14,'bold')).grid(row=0,column=0,columnspan=3,sticky='w',pady=(0,8))
        ttk.Label(f,text='Plaintext:').grid(row=1,column=0,sticky='nw')
        self.txt_plain = tk.Text(f,width=64,height=5,wrap='word')
        self.txt_plain.grid(row=1,column=1,columnspan=2,sticky='ew',pady=3)
        self.key, self.iv = tk.StringVar(), tk.StringVar()
        for row,label,var,button,fn in [(2,'Key (Base64):',self.key,'Generate Key',new_key),(3,'IV (Base64):',self.iv,'Generate IV',new_iv)]:
            ttk.Label(f,text=label).grid(row=row,column=0,sticky='w')
            ttk.Entry(f,textvariable=var,width=60).grid(row=row,column=1,sticky='ew',pady=3)
            ttk.Button(f,text=button,command=lambda v=var,g=fn:v.set(g())).grid(row=row,column=2,sticky='ew')
        ttk.Label(f,text='Ciphertext:').grid(row=4,column=0,sticky='nw')
        self.txt_cipher=tk.Text(f,width=64,height=5,wrap='char')
        self.txt_cipher.grid(row=4,column=1,columnspan=2,sticky='ew',pady=3)
        buttons=ttk.Frame(f); buttons.grid(row=5,column=1,columnspan=2,sticky='w',pady=6)
        for label,cmd in [('Encrypt',self.encrypt),('Decrypt',self.decrypt),('Save File',self.save),('Open File',self.open)]:
            ttk.Button(buttons,text=label,command=cmd).pack(side='left',padx=(0,8))
        self.status=tk.StringVar(value='Bấm Generate Key và Generate IV trước.')
        ttk.Label(f,textvariable=self.status,wraplength=650).grid(row=6,column=1,columnspan=2,sticky='w')

    def _get(self,w): return w.get('1.0','end-1c')
    def _set(self,w,value):
        w.delete('1.0','end'); w.insert('1.0',value)
    def encrypt(self):
        try:
            self._set(self.txt_cipher,encrypt_text(self._get(self.txt_plain),self.key.get(),self.iv.get()))
            self.status.set('Đã mã hóa AES-CBC.')
        except ValueError as ex: messagebox.showerror('Lỗi',str(ex))
    def decrypt(self):
        try:
            self._set(self.txt_plain,decrypt_text(self._get(self.txt_cipher),self.key.get(),self.iv.get()))
            self.status.set('Đã giải mã AES-CBC.')
        except ValueError as ex: messagebox.showerror('Lỗi',str(ex))
    def save(self):
        if not self._get(self.txt_cipher).strip():
            messagebox.showerror('Lỗi','Chưa có bản mã để lưu.'); return
        path=filedialog.asksaveasfilename(defaultextension='.txt',initialfile='aes_cbc_ciphertext.txt')
        if path:
            try:
                Path(path).write_text(to_file(self.iv.get(),self._get(self.txt_cipher)),encoding='utf-8')
                self.status.set('Đã lưu IV và bản mã: '+path)
            except OSError as ex: messagebox.showerror('Lỗi',str(ex))
    def open(self):
        path=filedialog.askopenfilename(filetypes=[('Text file','*.txt')])
        if path:
            try:
                iv,ciphertext=from_file(Path(path).read_text(encoding='utf-8'))
                self.iv.set(iv); self._set(self.txt_cipher,ciphertext)
                self.status.set('Đã mở tệp. Nhập đúng Key rồi bấm Decrypt.')
            except (ValueError,OSError) as ex: messagebox.showerror('Lỗi',str(ex))


def self_test():
    key,iv=new_key(),new_iv()
    for text in ['Xin chào AES-CBC','','1234567890123456','Văn bản dài tiếng Việt có dấu. '*200]:
        assert decrypt_text(encrypt_text(text,key,iv),key,iv)==text
    c=encrypt_text('abc',key,iv)
    assert len(base64.b64decode(c))==16
    assert from_file(to_file(iv,c))==(iv,c)
    # Deterministically break the final PKCS7 byte via IV, rather than a flaky random wrong-key test.
    bad_iv=bytearray(base64.b64decode(iv)); bad_iv[-1]^=13
    try:
        decrypt_text(c,key,base64.b64encode(bad_iv).decode())
    except ValueError: pass
    else: raise AssertionError('Padding hỏng phải bị từ chối')
    print('SELF-TEST OK: 4 round-trips, padding, file format, invalid padding.')


if __name__=='__main__':
    if '--self-test' in sys.argv: self_test()
    else: App().mainloop()
