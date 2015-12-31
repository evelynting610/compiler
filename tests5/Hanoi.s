	.file	"Hanoi.java"
	.section	.rodata
L5:	.string	"Move disk 1 from "
	.align 8
L6:	.string	" to "
	.align 8
L8:	.string	" from "
	.align 8
L0:	.string	"\nTotal moves: "
	.align 8
L1:	.string	"\n"
	.align 8
L7:	.string	"Move disk "
	.align 8

	.align 8

	.section	.data
	.local v_0
	.comm v_0,8,8

	.section	.text
	.align 8
	.globl main
	.type	main,@function
main:
	jmp	m_0
	.text
	.align 8
m_0:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L10:
	movq $0,%rdi
	leaq v_0(%rip),%rsi
	movq %rdi,(%rsi) 
	movq $9,%rdi
	movq $1,%rsi
	movq $2,%rdx
	callq m_1
	leaq v_0(%rip),%rdi
	movq (%rdi), %rdi
	callq b_intToString_0
	movq %rax, %rsi 
	leaq L0(%rip),%rdi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	leaq L1(%rip),%rsi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	callq b_printString_0
main_0:
	jmp L9
L9:
	leave
	ret


	.text
	.align 8
m_1:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$48, %rsp
L12:
	movq %r15,0(%rsp)
	movq %r14,8(%rsp)
	movq %r13,16(%rsp)
	movq %r12,24(%rsp)
	movq %rbx,32(%rsp)
	movq %rdx, %rbx 
	movq %rsi, %r12 
	movq %rdi, %r13 
	leaq v_0(%rip),%rdi
	movq (%rdi), %rdi
	movq %rdi, %rdi
	movq $1,%rsi
	addq %rsi, %rdi
	leaq v_0(%rip),%rsi
	movq %rdi,(%rsi) 
	movq $1,%rdi
	cmpq %rdi, %r13 
	je L2
L3:
	movq $6,%rdi
	movq %rdi, %rdi
	subq %r12, %rdi
	movq %rdi, %r14
	subq %rbx, %r14
	movq %r13, %rdi
	movq $1,%rsi
	subq %rsi, %rdi
	movq %r12, %rsi
	movq %r14, %rdx
	callq m_1
	movq %r13, %rdi
	callq b_intToString_0
	movq %rax, %rsi 
	leaq L7(%rip),%rdi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	leaq L8(%rip),%rsi
	callq b_stringConcatenate_0
	movq %rax, %r15 
	movq %r12, %rdi
	callq b_intToString_0
	movq %rax, %rsi 
	movq %r15, %rdi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	leaq L6(%rip),%rsi
	callq b_stringConcatenate_0
	movq %rax, %r12 
	movq %rbx, %rdi
	callq b_intToString_0
	movq %rax, %rsi 
	movq %r12, %rdi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	leaq L1(%rip),%rsi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	callq b_printString_0
	movq %r13, %rdi
	movq $1,%rsi
	subq %rsi, %rdi
	movq %r14, %rsi
	movq %rbx, %rdx
	callq m_1
L4:
hanoi_0:
	movq 32(%rsp),%rbx
	movq 24(%rsp),%r12
	movq 16(%rsp),%r13
	movq 8(%rsp),%r14
	movq 0(%rsp),%r15
	jmp L11
L2:
	movq %r12, %rdi
	callq b_intToString_0
	movq %rax, %rsi 
	leaq L5(%rip),%rdi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	leaq L6(%rip),%rsi
	callq b_stringConcatenate_0
	movq %rax, %r12 
	movq %rbx, %rdi
	callq b_intToString_0
	movq %rax, %rsi 
	movq %r12, %rdi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	leaq L1(%rip),%rsi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	callq b_printString_0
	jmp L4
L11:
	leave
	ret


