/**
 * Auto Generated Java Class.
 */
public class RPTransformT2 implements Transform {
    private static final byte[] T2StartTag = "<BBB>".getBytes();
    private static final byte[] T2EndTag = "</BBB>".getBytes();
    
    private void toLower (byte[] in, int start, int end) {
        if ((start > in.length) || (end > in.length) || (start >= end))
            return;
        
        while (start < end) {
            if ((in[start] > 0x40) && (in[start] < 0x5B)) {
                in[start++] |= 0x20;
            } else {
                start++;
            }
        }      
    }
        
    /* ADD YOUR CODE HERE */
    public byte[] transform(byte[] inData) throws Exception {
        int fromIndex = 0, marker = 0;
        
        if (inData == null) return null;
        
        while ((marker = KPM.indexOf(inData, fromIndex, inData.length, T2StartTag)) != -1) {
            fromIndex = marker + T2StartTag.length;
            //Search for end tag
            if ((marker = KPM.indexOf(inData, fromIndex, inData.length, T2EndTag)) == -1) {
                //No end tag found
                //check if there is another start tag
                if ((marker = KPM.indexOf(inData, fromIndex, inData.length, T2StartTag)) != -1) {
                    //reached max limit
                    if (fromIndex > inData.length) break;
                    continue;
                }
                System.err.println("T2 end tag not found; T2 operation skipped");
                return inData;
            }
            //transform to lower
            toLower(inData, fromIndex, marker);
            //update the location to continue from
            fromIndex = marker + T2EndTag.length;
            //reached max size
            if (fromIndex > inData.length) break;
        }

        return(inData);
    }
}
