import java.nio.ByteBuffer;
    
/**
 * Auto Generated Java Class.
 */
public class RPTransformT1 implements Transform {
    private static final byte[] T1StartTag = "<AAA>".getBytes();
    private static final byte[] T1EndTag = "</AAA>".getBytes();
    private static final byte[] T1Replace = "YYY".getBytes();
  
    public byte[] transform(byte[] inData) throws Exception {
        ByteBuffer outData = ByteBuffer.allocate(inData.length + T1Replace.length);
        int fromIndex = 0, marker = 0;
        boolean tagsFound = false;
        
        outData.clear();
        if (inData == null) return null;
        while ((marker = KPM.indexOf(inData, fromIndex, inData.length, T1StartTag)) != -1) {
            //Add data upto the first T1TAG
            outData.put(inData, fromIndex, marker - fromIndex);
            //Append the T1 start TAG 
            outData.put(T1StartTag);
            //Skip over the start tag to search for end tag
            fromIndex = marker + T1StartTag.length;
            //search for end tag
            if ((marker = KPM.indexOf(inData, fromIndex, inData.length, T1EndTag)) == -1) {
                //No end tag found
                //Check if there are other start tags
                if ((marker = KPM.indexOf(inData, fromIndex, inData.length, T1StartTag)) != -1) {
                    //reached max data then break else continue processing
                    if (fromIndex > inData.length) break;
                    continue;
                }
                System.err.println("T1 end tag not found; T1 operation skipped");
                //Add remainder of string after last T1 Tag
                outData.put(inData, fromIndex, inData.length - fromIndex);
                outData.flip();
                return outData.array();
            }
            tagsFound = true;
            //Append replacement text
            outData.put(T1Replace);
            //Append T1 end TAG
            outData.put(T1EndTag);
            fromIndex = marker + T1EndTag.length;
            if (fromIndex > inData.length) break;
        }
        
        //If we found atleast one tag then return outData 
        if (tagsFound) {
            //Add remainder of string
            outData.put(inData, fromIndex, inData.length - fromIndex);
            outData.flip();
            return outData.array();
        }
        // No instance of T1 tag pair found, return inData intact
        return(inData);
    }
}
