/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket.codec;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import org.springframework.core.codec.Encoder;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.protobuf.ProtobufHttpMessageWriter;
import org.dbs24.entity.*;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageEncoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.lang.Nullable;

@Log4j2
@Deprecated
public class RetailLoanContractEncoder<T extends RetailLoanContract> implements HttpMessageEncoder<T> {

    @Override
    public boolean canEncode(ResolvableType rt, @Nullable MimeType mt) {
        log.debug("add AbstractRetailLoanContract");
        return true;
    }

    @Override
    public Flux<DataBuffer> encode(Publisher<? extends T> pblshr, DataBufferFactory dbf, ResolvableType rt, @Nullable MimeType mt, @Nullable Map<String, Object> map) {
        log.debug("add AbstractRetailLoanContract");
        return null;
    }

    @Override
    public List<MediaType> getStreamingMediaTypes() {
        log.debug("add AbstractRetailLoanContract");
        return new ArrayList<>();
    }

    @Override
    public List<MimeType> getEncodableMimeTypes() {
        final Collection<MimeType> lmt = ServiceFuncs.<MimeType>createCollection();
        log.debug("get codecs");
        return (List) lmt;
    }
}
