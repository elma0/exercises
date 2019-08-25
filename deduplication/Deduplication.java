package deduplication;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

public class Deduplication {

    static char[] separartor = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator")).toCharArray();

    public static void main(String[] args) throws IOException {
        Charset cs = Charset.forName("UTF-8");
        CharsetEncoder encoder = cs.newEncoder();
        try (
                FileInputStream stream = new FileInputStream("deduplication/input2.txt");
                FileChannel channel = stream.getChannel();
                Iter iter = new Iter(channel);
                WritableByteChannel outChannel = Channels.newChannel(System.out)
        ) {
            ByteBuffer out = ByteBuffer.allocateDirect(12 + separartor.length);
            CharBuffer charBuffer = CharBuffer.allocate(12 + separartor.length);
            CharBuffer pivot = CharBuffer.allocate(13 + separartor.length);
            pivot.put('-');
            pivot.position(12 + separartor.length);
            int size = read(iter, charBuffer);
            int len = charArrayToInt(charBuffer, size);
            for (int i = 0; i < len; i++) {
                if (read(iter, charBuffer) == 0) {
                    break;
                }
                if (compare(charBuffer, pivot) == 1) {
                    int pos = charBuffer.position();
                    charBuffer.put(separartor);
                    charBuffer.rewind();
                    encode(encoder, charBuffer, out);
                    outChannel.write(out);
                    copy(charBuffer, pivot, pos);
                }
            }
        }
    }

    private static void copy(CharBuffer source, CharBuffer dest, int pos) {
        dest.rewind();
        source.rewind();
        dest.put(source.array());
        dest.position(pos);
    }

    private static int compare(CharBuffer a1, CharBuffer a2) {
        int pos1 = a1.position();
        int pos2 = a2.position();
        int l1 = a1.position() * (a1.get(0) == '-' ? -1 : 1);
        int l2 = a2.position() * (a2.get(0) == '-' ? -1 : 1);
        try {
            if (l1 > l2) {
                return 1;
            } else if (l1 < l2) {
                return -1;
            } else {
                int limit = a1.position();
                a1.rewind();
                a2.rewind();
                for (int i = 0; i < limit; i++) {
                    if (a1.get(i) > a2.get(i)) {
                        return 1;
                    } else if (a1.get(i) < a2.get(i)) {
                        return -1;
                    }
                }
                return 0;
            }
        } finally {
            a1.position(pos1);
            a2.position(pos2);
        }
    }

    static class Iter implements Closeable {
        private final FileChannel channel;
        private final ByteBuffer buf = ByteBuffer.allocateDirect(8192);
        private final Charset cs = Charset.forName("UTF-8");
        private final CharsetDecoder decoder = cs.newDecoder();
        private final CharBuffer chbuf = CharBuffer.allocate((int) ((8192 * decoder.maxCharsPerByte())));
        private int read;

        Iter(FileChannel channel) throws IOException {
            this.channel = channel;
            if ((read = channel.read(buf)) != -1) {
                buf.rewind();
                decode(buf, chbuf, read);
                buf.rewind();
            }
        }

        private void decode(ByteBuffer buf, CharBuffer chbuf, int limit) {
            chbuf.rewind();
            buf.limit(limit);
            chbuf.limit(limit);
            CoderResult result = decoder.decode(buf, chbuf, true);
            if (result.isUnderflow()) {
                chbuf.flip();
            } else if (result.isOverflow()) {
                throw new RuntimeException("overflow");
            }
        }

        boolean hasNext() throws IOException {
            if (chbuf.hasRemaining()) {
                return true;
            } else {
                if ((read = channel.read(buf)) != -1) {
                    buf.rewind();
                    decode(buf, chbuf, read);
                    buf.rewind();
                    return true;
                }
                return false;
            }
        }

        char next() {
            return chbuf.get();
        }

        @Override
        public void close() throws IOException {
            channel.close();
        }
    }

    private static int read(Iter reader, CharBuffer in) throws IOException {
        in.limit(12 + separartor.length);
        in.rewind();
        if (!reader.hasNext()) {
            return 0;
        }
        char c;
        int i = 0;
        while (reader.hasNext() && (c = reader.next()) != '\n') {
            if (c == 0) {
                in.limit(in.position() + separartor.length);
                return i;
            }
            if (c == '\r') {
                continue;
            }
            in.put(c);
            i++;
        }
        in.limit(in.position() + separartor.length);
        return i;
    }

    private static void encode(CharsetEncoder encoder, CharBuffer chbuf, ByteBuffer out) {
        out.rewind();
        chbuf.rewind();
        out.limit(chbuf.limit());
        CoderResult result = encoder.encode(chbuf, out, true);
        if (result.isUnderflow()) {
            out.flip();
        } else if (result.isOverflow()) {
            throw new RuntimeException("overflow");
        }
    }

    private static int charArrayToInt(CharBuffer data, int l) {
        data.rewind();
        int result = 0;
        int intIndex = 0;
        int mul = 1;
        if (data.get(0) == '-') {
            intIndex++;
            mul = -1;
        }
        int digit;
        for (; intIndex < l; intIndex++) {
            digit = data.get(intIndex) - '0';
            if ((digit < 0) || (digit > 9)) return result;
            result = result * 10 + digit;
        }
        return result * mul;
    }
}
